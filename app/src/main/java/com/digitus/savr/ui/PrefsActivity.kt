package com.digitus.savr.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.documentfile.provider.DocumentFile
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.digitus.savr.SavrApplication.Companion.appDataDir
import com.digitus.savr.R
import com.digitus.savr.ui.utils.DATA_DIR_URI
import com.digitus.savr.ui.utils.LOGTAG
import com.digitus.savr.ui.utils.arePermissionsGranted
import com.digitus.savr.ui.utils.prefsGetString
import com.digitus.savr.ui.utils.prefsStoreString
import com.digitus.savr.ui.utils.setDirectories


class PrefsActivity : AppCompatActivity() {

    fun openDocumentTree() {
        prefsStoreString(applicationContext, DATA_DIR_URI, "")
        val uriString = prefsGetString(applicationContext, DATA_DIR_URI)
        when {
            uriString == null || uriString == "" -> {
                Log.w(LOGTAG, "uri not stored")
                askPermissionDir()
            }
            arePermissionsGranted(applicationContext, uriString) -> {   }
            else -> {
                Log.w(LOGTAG, "uri permission not stored")
                askPermissionDir()
            }
        }
    }


    // Receiver
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK){

                val data = it.data

                if (data != null) {
                    //this is the uri user has provided us
                    val treeUri: Uri? = data.data
                    if (treeUri != null) {
                        Log.i(LOGTAG, "giving access to URI: ${treeUri.toString()}")
                        // here we should do some checks on the uri, we do not want root uri
                        // because it will not work on Android 11, or perhaps we have some specific
                        // folder name that we want, etc
                        if (Uri.decode(treeUri.toString()).endsWith(":")){
                            Toast.makeText(applicationContext,"Cannot use root folder!", Toast.LENGTH_SHORT).show()
                            // consider asking user to select another folder
//                            return
                        } else {
                            // here we ask the content resolver to persist the permission for us
                            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            contentResolver.takePersistableUriPermission(
                                treeUri,
                                takeFlags
                            )

                            // we should store the string fo further use
                            prefsStoreString(applicationContext, DATA_DIR_URI, treeUri.toString())

                            //Finally, we can do our file operations
                            //Please note, that all file IO MUST be done on a background thread. It is not so in this
                            //sample - for the sake of brevity.
//                    makeDoc(treeUri)

                            val dir = DocumentFile.fromTreeUri(this, treeUri)

                            //        if (dir == null || !dir.exists()) {
//            //the folder was probably deleted
//            Log.e(LOGTAG, "no Dir")
//            //according to Commonsware blog, the number of persisted uri permissions is limited
//            //so we should release those we cannot use anymore
//            //https://commonsware.com/blog/2020/06/13/count-your-saf-uri-permission-grants.html
//            releasePermissions(dirUri)
//            //ask user to choose another folder
//            Toast.makeText(applicationContext,"Folder deleted, please choose another!",Toast.LENGTH_SHORT).show()
//            openDocumentTree()
//        }

                            if (dir != null) {
                                var files = dir.listFiles()
                                Log.d(LOGTAG, files.map { it.name }.toString())
                            }

                            setDirectories(applicationContext)
                        }
                    }
                }
            }
        }

    // this will present the user with folder browser to select a folder for our data
    private fun askPermissionDir() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        getResult.launch(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.my_settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setDirectories(applicationContext)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        var dirStr = mutableStateOf(appDataDir?.name?: "(unknown)")

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

//            val appContext = (activity as PrefsActivity?)?.applicationContext

//            if (appContext != null) {
////                setDirectories(appContext)
//                prefsGetString(appContext, DATA_DIR_URI, "")
//            }

            findPreference<Preference>("dataDir")?.summary = appDataDir?.name

//            dirStr.setValue(appDataDir?.name.toString())

//            findPreference<Preference>("dataDir")?.summary =
//            dirStr

        findPreference<Preference>("dataDir")
            ?.setOnPreferenceClickListener {
                Log.i("Preferences", "dataDir was clicked")

                (activity as PrefsActivity?)?.openDocumentTree()

//                it.summary= dirStr.toString()

//                it.summary="set?"t
                // TODO: https://stackoverflow.com/questions/66494520/is-there-a-way-to-dynamically-change-the-string-in-text-of-compose

                if (context != null) {
//                    Uri.parse(prefsGetString(context, DATA_DIR_URI, ""))
                }

                true // Return true if the click is handled.
            }

        }

    }
}


//TODO: add preview to this?
