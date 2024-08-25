package com.digitus.savr.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import java.text.Normalizer
import java.util.Locale


import androidx.documentfile.provider.DocumentFile
import com.digitus.savr.SavrApplication.Companion.appDataDir
import com.digitus.savr.SavrApplication.Companion.appSavesDir
import com.digitus.savr.R
import com.digitus.savr.model.Article
import com.digitus.savr.model.ReadabilityResult
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


const val PREFS_FILENAME = "com.device.savr.prefs"
const val DATA_DIR_URI = "folder_uri"

const val LOGTAG = "MainActivity"

val JS_SCRIPT_READABILITY = """
                            let readabilityResult = new Readability(document).parse();

                            if (readabilityResult === null) {
                              throw new Error('Readability did not parse');
                            }

                            readabilityResult

                            """.trimIndent()


fun toUrlSlug(title: String): String {
    // Normalize the string and remove diacritics (accents)
    val normalizedTitle = Normalizer.normalize(title, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")

    // Convert to lowercase, trim spaces, replace spaces with hyphens, and remove non-alphanumeric characters
    var slug = normalizedTitle.lowercase(Locale.getDefault())
        .trim()
        .replace("\\s+".toRegex(), "-")
        .replace("[^a-z0-9-]".toRegex(), "")

    // Limit the slug to 64 characters
    if (slug.length > 64) {
        slug = slug.substring(0, 64).trimEnd('-')
    }

    return slug
}


fun prefsStoreString(context: Context, key: String, text: String) {
    val editor = context.getSharedPreferences(PREFS_FILENAME, 0)!!.edit()
    editor.putString(key, text)
    editor.commit()
}

//fun prefsGetString() {
//    val preferences = PreferenceManager.getDefaultSharedPreferences(context).all
//
//    preferences.forEach {
//        Log.d("Preferences", "${it.key} -> ${it.value}")
//    }
//}

fun prefsGetString(context: Context, key: String): String? {
    val text = context.getSharedPreferences(PREFS_FILENAME, 0).getString(key, null)
    return text
}

//
//fun prefsGetString(context: Context, key: String, def: String): String? {
//    val text = context.getSharedPreferences(PREFS_FILENAME, 0).getString(key, def)
//    return text
//}


fun arePermissionsGranted(context: Context, uriString: String): Boolean {
    // list of all persisted permissions for our app
    val list = context.contentResolver.persistedUriPermissions
    for (i in list.indices) {
        val persistedUriString = list[i].uri.toString()
        //Log.d(LOGTAG, "comparing $persistedUriString and $uriString")
        if (persistedUriString == uriString && list[i].isWritePermission && list[i].isReadPermission) {
            //Log.d(LOGTAG, "permission ok")
            return true
        }
    }
    return false
}



fun setDirectories(context: Context) {

    val dataDirStr = prefsGetString(context, DATA_DIR_URI)

    appDataDir = null

    if (dataDirStr != null && dataDirStr != "") {
        val dataDirUri = Uri.parse(dataDirStr)
        appDataDir = DocumentFile.fromTreeUri(context, dataDirUri)
    }

    if (appDataDir == null) {
//            TODO: error
    } else if (appDataDir?.exists() ?:false) {
        if (appDataDir?.isDirectory() ?: false) {
            appSavesDir = appDataDir?.findFile("saves")
        } else if (appDataDir?.isFile ?: false) {
            // TOOD: error
        } else {
            appSavesDir = appDataDir?.createDirectory("saves")
        }
    }

    Log.i(LOGTAG, "appDataDir ${appDataDir?.name}")
    Log.i(LOGTAG, "appSavesDir ${appSavesDir?.name}")

}

fun chooseUri(context: Context, treeUri: Uri) {

    Log.i(LOGTAG, "giving access to URI: ${treeUri.toString()}")
    // here we should do some checks on the uri, we do not want root uri
    // because it will not work on Android 11, or perhaps we have some specific
    // folder name that we want, etc
    if (Uri.decode(treeUri.toString()).endsWith(":")) {
        Toast.makeText(context, "Cannot use root folder!", Toast.LENGTH_SHORT).show()
        // consider asking user to select another folder
        return
    }
    // here we ask the content resolver to persist the permission for us
    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    context.contentResolver.takePersistableUriPermission(
        treeUri,
        takeFlags
    )

    // we should store the string fo further use
    prefsStoreString(context, DATA_DIR_URI, treeUri.toString())


    val dir = DocumentFile.fromTreeUri(context, treeUri)
    if (dir != null) {
        var files = dir.listFiles()
        Log.d(LOGTAG, files.map { it.name }.toString())
    }

    setDirectories(context)

}



fun createFileText(context: Context, filename: String, content: String, slug: String, mimeType: String ="*/html") {

    if (appSavesDir == null) {
        Toast.makeText(context,"Error creating directory",Toast.LENGTH_SHORT).show()
        return
    }

    var articleDir = appSavesDir?.findFile(slug)

    if (articleDir == null) {
        articleDir = appSavesDir?.createDirectory(slug)

        if (articleDir == null) {
            Toast.makeText(context,"Error creating directory",Toast.LENGTH_SHORT).show()
            return
        }
        Log.i(LOGTAG, "created article dir: ${articleDir.name}")
    }

    val existingFile = articleDir.findFile(filename)

    if (existingFile != null && existingFile.isFile) {
        existingFile.delete()
        // TODO: if file exists delete it first
    }

    val file = articleDir.createFile(mimeType, filename)
    if (file != null && file.canWrite()) {
        Log.d(LOGTAG, "file.uri = ${file.uri.toString()}")
        writeText(context, file.uri, content)
        Log.d(LOGTAG, "finished writing file $filename")
    } else {
        Log.d(LOGTAG, "can not create file")
        //consider showing some more appropriate error message
        Toast.makeText(context,"Write error!", Toast.LENGTH_SHORT).show()
    }

}

//    private fun releasePermissions(uri: Uri) {
//        val flags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
//                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//        contentResolver.releasePersistableUriPermission(uri,flags)
//        //we should remove this uri from our shared prefs, so we can start over again next time
//        prefsStoreString(DATA_DIR_URI, "")
//    }


//Just a test function to write something into a file, from https://developer.android.com
//Please note, that all file IO MUST be done on a background thread. It is not so in this
//sample - for the sake of brevity.
fun writeText(context: Context, uri: Uri, content: String): Boolean {
    try {

        context.contentResolver.openFileDescriptor(uri, "w")?.use { parcelFileDescriptor ->
            FileOutputStream(parcelFileDescriptor.fileDescriptor).use {
                it.write(
                    content.toByteArray()
                )

//                runOnUiThread {
                    Toast.makeText(context, "File Write OK!", Toast.LENGTH_SHORT)
                        .show()
//                }
            }
        }

        return true
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return false
}

@Throws(IOException::class)
fun readTextFromUri(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val reader = BufferedReader(
        InputStreamReader(
            inputStream
        )
    )
    val stringBuilder = StringBuilder()
    var line: String?
    while ((reader.readLine().also { line = it }) != null) {
        stringBuilder.append(line)
    }
    inputStream?.close()
    return stringBuilder.toString()
}



fun formatHtml(article: Article, html: String?): String {

//    content: String, title: String, dateString: String?, author: String?, readMinutes: Int?): String {

    // TODO: handle blank time, author etc

    val formatted = """
          <!DOCTYPE html>
          <head>
           <title>
              Savr - $article.title
            </title>
            <meta content="text/html; charset=UTF-8" http-equiv="content-type">

            <link rel="stylesheet" href="article.css">
            <link rel="stylesheet" href="/assets/article.css">
            <link rel="stylesheet" href="file:///android_asset/article.css">

            <!--
            savr meta...
            
            download platform: android

            parser: readability?
            -->

            </head>
            
            <body>

              <div id="contentRoot">
                <h1>${article.title}</h1>
                <div class="byline">By ${article.author}</div>
                <div class="published">domain dot ${article.publishedDate}</div>
                <div class="readTime">${article.readTimeMinutes} minute read</div>
                <hr />
                ${html}
              </div>

            </body></html>
          """

    return formatted
}


fun readFromAsset(context: Context, name: String): String {

    var string = ""

    try {
        val inputStream: InputStream = context.assets.open(name)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        string = String(buffer)
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return string
}


fun calcReadingTime(text: String): Int {

    val wordCount = text.split("\\s+".toRegex()).size

    val wordsPerMinute = 200  // adjust this value if needed

    val readingTimeMinutes = wordCount.toDouble() / wordsPerMinute

    val roundedReadingTime = kotlin.math.ceil(readingTimeMinutes).toInt()

//    println("Estimated reading time: $roundedReadingTime minute(s)")
    return roundedReadingTime
}

fun readabilityToArticle(readabilityResult: ReadabilityResult, url: String) : Article{

    val content = readabilityResult.content!!
    val title = readabilityResult.title
    val date = readabilityResult.publishedTime
    val author = readabilityResult.byline ?: "unknown author"

    val slug = toUrlSlug(title)

    val currentDateTimeWithZone = ZonedDateTime.now()

    // Format it as an ISO 8601 string with timezone information
    val nowString = currentDateTimeWithZone.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)

    val article = Article(
        title = title,
        slug = slug,
        url = url,
        state = "unread",
        author = author,
        readTimeMinutes = calcReadingTime(readabilityResult.textContent!!),
        publishedDate = date,
        html = content,
//        html = "(saved in readability.html)",
        ingestPlatform = "android",
        ingestDate = nowString,
        imageId = R.drawable.post_6,
        imageThumbId = R.drawable.post_6_thumb
    )

    return article
}