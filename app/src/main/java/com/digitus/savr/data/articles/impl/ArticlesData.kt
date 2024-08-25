
@file:Suppress("ktlint:max-line-length") // String constants read better
package com.digitus.savr.data.articles.impl

import com.digitus.savr.R
import com.digitus.savr.model.Article
import com.digitus.savr.model.ArticlesFeed

/**
 * Define hardcoded posts to avoid handling any non-ui operations.
 */

val article1 = Article(
    slug = "i-spent-a-week-without-ipv4-to-understand-ipv6-transition-mechan",
    title = "I spent a Week without IPv4 to Understand IPv6 Transition",
    //                        subtitle = "How to configure your module paths, instead of using Gradle’s default.",
    url = "https://medium.com/androiddevelopers/gradle-path-configuration-dc523f0ed25c",
    //                        publication = publication,
    author = "John Doe",
    readTimeMinutes = 10,
    publishedDate = "August 24",
    html = "<h1> hello world! </h2",

    imageId = R.drawable.post_6,
    imageThumbId = R.drawable.post_6_thumb
)

val article2 = Article(
    slug = "installation-gitbook",
    title = "Installation Gitbook",
    //                        subtitle = "How to configure your module paths, instead of using Gradle’s default.",
    url = "https://medium.com/androiddevelopers/gradle-path-configuration-dc523f0ed25c",
    //                        publication = publication,
    author = "Johny B",
    readTimeMinutes = 3,
    publishedDate = "August 24",
    html = "<h1> hello world! </h2",

    imageId = R.drawable.post_1,
    imageThumbId = R.drawable.post_1_thumb
)

val articles: ArticlesFeed =
    ArticlesFeed(
        saved = mutableListOf(article1, article2)
    )
