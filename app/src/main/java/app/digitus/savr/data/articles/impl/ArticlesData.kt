
@file:Suppress("ktlint:max-line-length") // String constants read better
package app.digitus.savr.data.articles.impl

import app.digitus.savr.R
import app.digitus.savr.model.Article
import app.digitus.savr.model.ArticlesFeed

/**
 * Define hardcoded posts to avoid handling any non-ui operations.
 */


val article_dummy = Article(
    slug = "dummy-missing-article",
    title = "its just not here",
    //                        subtitle = "How to configure your module paths, instead of using Gradle’s default.",
    url = "https://google.com",
    //                        publication = publication,
    author = "John Doe",
    readTimeMinutes = 10,
    publishedDate = "August 24",
//    html = "<h1> hello world! </h2",
)



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
)

val articles: ArticlesFeed =
    ArticlesFeed(
        all = mutableListOf(article1, article2)
    )
