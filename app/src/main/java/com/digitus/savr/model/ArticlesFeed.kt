package com.digitus.savr.model


data class ArticlesFeed(
    val saved: MutableList<Article>,
) {
    val allArticles: List<Article> = saved
}
