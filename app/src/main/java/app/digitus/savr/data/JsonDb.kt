package app.digitus.savr.data

import android.content.Context
import android.util.Log
import app.digitus.savr.SavrApplication.Companion.appDataDir
import app.digitus.savr.SavrApplication.Companion.jsonDbFile
import app.digitus.savr.model.Article
import app.digitus.savr.model.Saves
import app.digitus.savr.utils.DbCreationException
import app.digitus.savr.utils.LOGTAG
import app.digitus.savr.utils.readTextFromUri
import app.digitus.savr.utils.setDirectories
import app.digitus.savr.utils.writeText
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException

const val DB_FILENAME = "saves.json"

const val empty_saves_content = """{"saves" : []}"""

class JsonDb(val context: Context) {

    init {

        if (appDataDir == null) {
            setDirectories(context)
        }

        if (appDataDir!=null && jsonDbFile == null) {
            jsonDbFile = appDataDir?.findFile(app.digitus.savr.data.DB_FILENAME)

            if (jsonDbFile == null) {
                jsonDbFile = appDataDir?.createFile(
                    "application/json", app.digitus.savr.data.DB_FILENAME
                ) ?: error("Error creating db file")

                if (jsonDbFile != null && (jsonDbFile ?: error("Db file is empty")).canWrite()) {

                    val written = writeText(context, (jsonDbFile ?: error("Db file is empty")).uri,
                        app.digitus.savr.data.empty_saves_content
                    )
                    if (!written) {
                        throw AssertionError("Error writing to database")
                    }

                } else {
                    Log.d(LOGTAG, "Error creating database")
                    throw DbCreationException("Error creating database")
                }

            }
        }
    }

    fun getEverything() : Saves {
        if (jsonDbFile == null) {
//            TODO: should probably make them set the dir first
            return Gson().fromJson(app.digitus.savr.data.empty_saves_content, Saves::class.java)
//            return dbModel
        }
        val fileString = readTextFromUri(context, (jsonDbFile ?: error("Db file is empty")).uri)

        try {
            val dbModel: Saves = Gson().fromJson(fileString, Saves::class.java)
            return dbModel
        } catch (e: JsonSyntaxException) {
            throw AssertionError("Database corrupt", e)
        }
    }

    fun getReadable(): List<Article> {

        val dbModel = getEverything()

        dbModel.saves = dbModel.saves.filter { it.state != "archived" && it.state != "deleted" }.toMutableList()

        return dbModel.saves
    }

    fun getArchived(): List<Article> {

        val dbModel = getEverything()

        dbModel.saves = dbModel.saves.filter { it.state == "archived"  }.toMutableList()

        return dbModel.saves
    }

    fun articleInDb(article: Article): Article? {

        val dbModel = getEverything()

        val responseArticle: Article? = dbModel.saves.find { it.slug == article.slug }

        return responseArticle

    }

    fun addArticle(article: Article) {

        val dbModel = getEverything()

        if (dbModel.saves.find { it.slug == article.slug } != null) {
            Log.d(LOGTAG, "skipping db add. article already exists: ${article.slug}")
            throw Exception("Article already exists")
        } else {
            dbModel.saves.add(0, article)

            val outString: String = GsonBuilder().setPrettyPrinting().create().toJson(dbModel)

            writeText(context, (jsonDbFile ?: error("Db file is empty")).uri, outString)
        }
    }

    fun archiveArticle(article: Article, unarchive: Boolean = false) {

        val dbModel = getEverything()

        val articleIndex = dbModel.saves.indexOfLast { it.slug == article.slug }

        if (articleIndex != -1) {
            if (unarchive) {
                dbModel.saves[articleIndex].state = "unread"
            } else {
                dbModel.saves[articleIndex].state = "archived"
            }

            val outString: String = GsonBuilder().setPrettyPrinting().create().toJson(dbModel)

            writeText(context, (jsonDbFile ?: error("Db file is empty")).uri, outString)
            Log.d(LOGTAG,"Article archived ${article.slug}")
        }
    }

    fun deleteArticle(article: Article) {

        val dbModel = getEverything()

//        val articleInDb = dbModel.saves.find { it.slug == article.slug }

        val articleIndex = dbModel.saves.indexOfLast { it.slug == article.slug }

        if (articleIndex != -1) {
            dbModel.saves.removeAt(articleIndex)

            val outString: String = GsonBuilder().setPrettyPrinting().create().toJson(dbModel)

            writeText(context, (jsonDbFile ?: error("Db file is empty")).uri, outString)
            Log.d(LOGTAG,"Article deleted ${article.slug}")
        }
    }


}
