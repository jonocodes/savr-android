
package app.digitus.savr.model

import app.digitus.savr.utils.parseReadabilityDate
import kotlinx.serialization.Serializable
import java.net.URI
import java.time.format.DateTimeFormatter

@Serializable
data class Article(
    val slug: String,
    val title: String,
    val url: String,
    var state: String = "unread", // unread, reading, finished, archived, deleted, ingesting
    val subtitle: String? = null,
    val publication: String? = null,
    val author: String? = null,
    val publishedDate: String? = null, // TODO: perhaps this should be a datetime object
    val ingestDate: String? = null,
    val ingestPlatform: String? = null, // platform/web
    val readTimeMinutes: Int? = null,
    var html: String? = null, // TODO: load this only in the article screen
) {

    fun domain(): String {
        val uri = URI(url)
        val domain: String = uri.host
        return domain.removePrefix("www.")
    }

    fun byline(): String {
        if (author == null) {
            return domain()
        }
        return author
    }

    fun publishedDateReadable(): String? {

//        TODO: make this nicer to read like "November 20, 2004", but localized

        if (publishedDate == null) {
            return null
        }

        val dt = parseReadabilityDate(publishedDate)

        return dt?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

}

@Serializable
data class Saves(
    var saves: MutableList<Article>
)
