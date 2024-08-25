package com.digitus.savr.data

import android.content.Context
import android.util.Log
import com.digitus.savr.SavrApplication.Companion.appDataDir
import com.digitus.savr.SavrApplication.Companion.jsonDbFile
import com.digitus.savr.model.Article
import com.digitus.savr.model.Saves
import com.digitus.savr.ui.utils.LOGTAG
import com.digitus.savr.ui.utils.readTextFromUri
import com.digitus.savr.ui.utils.setDirectories
import com.digitus.savr.ui.utils.writeText
import com.google.gson.Gson
import com.google.gson.GsonBuilder

const val DB_FILENAME = "saves.json"

class JsonDb(val context: Context) {

    init {

        if (appDataDir == null) {
            setDirectories(context)
        }

        if (jsonDbFile == null) {
            jsonDbFile = appDataDir?.findFile(DB_FILENAME)

            if (jsonDbFile == null) {
                jsonDbFile = appDataDir?.createFile("application/json", DB_FILENAME)

                if (jsonDbFile != null && jsonDbFile!!.canWrite()) { // TODO: !! bad!

                    writeText(context, jsonDbFile!!.uri, """
                          {"saves" : []}
                        """)

                } else {

                    Log.d(LOGTAG, "can not JsonDb file")
                    // TODO: showing some more appropriate error message
//                    Toast.makeText(context,"Error creating database!", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


    fun getEverything() : Saves {
        if (jsonDbFile == null) {
//            TODO: should probably make them set the dir first
            val dbModel: Saves = Gson().fromJson("""{"saves": []}""", Saves::class.java)
            return dbModel
        }
        val fileString = readTextFromUri(context, jsonDbFile!!.uri)

        val dbModel: Saves = Gson().fromJson(fileString, Saves::class.java)
        return dbModel
    }

    fun addArticle(article: Article) {

//        val fileString = readTextFromUri(context, jsonDbFile!!.uri)
//        val dbModel: Saves = Gson().fromJson(fileString, Saves::class.java)

        val dbModel = getEverything()

        if (dbModel.saves.find { it.slug == article.slug } != null) {
            Log.d(LOGTAG, "skipping db add. article already exists: ${article.slug}")
//            TODO: bubble up error
        } else {

            dbModel.saves.add(0, article)

            val outString: String = GsonBuilder().setPrettyPrinting().create().toJson(dbModel)

            writeText(context, jsonDbFile!!.uri, outString)
        }
    }


}
