
package com.digitus.savr.model

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val slug: String,
    val title: String,
    val url: String,
    val state: String = "unread", // unread, reading, finished, archived, deleted, ingesting
    val subtitle: String? = null,
    val publication: String? = null,
    val author: String? = null,
    val publishedDate: String? = null,
    val ingestDate: String? = null,
    val ingestPlatform: String? = null, // platform/web
    val readTimeMinutes: Int? = null,
    var html: String? = null, // TODO: load this only in the article screen
    @DrawableRes val imageId: Int?,
    @DrawableRes val imageThumbId: Int
)

@Serializable
data class Saves(
    val saves: MutableList<Article>
)
