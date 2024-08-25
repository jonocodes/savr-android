
package com.digitus.savr.data.articles.impl

import com.digitus.savr.SavrApplication
import com.digitus.savr.data.JsonDb
import com.digitus.savr.data.Result
import com.digitus.savr.data.articles.ArticlesRepository
import com.digitus.savr.model.Article
import com.digitus.savr.model.ArticlesFeed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

/**
 * Implementation of ArticlesRepository that returns a hardcoded list of
 * articles with resources after some delay in a background thread.
 */
class SavedArticlesRepository : ArticlesRepository {

    private val articlesFeed = MutableStateFlow<ArticlesFeed?>(null)

    override suspend fun getArticle(articleSlug: String?): Result<Article> {
        return withContext(Dispatchers.IO) {
            val article = articles.allArticles.find { it.slug == articleSlug }

            if (article == null) {
                Result.Error(IllegalArgumentException("Article not found"))
            } else {
                Result.Success(article)
            }
        }
    }

    override suspend fun getArticlesFeed(): Result<ArticlesFeed> {
        return withContext(Dispatchers.IO) {

            val everything = JsonDb(SavrApplication.appContext!!).getEverything()
            val myArticles = ArticlesFeed(saved = everything.saves)

            articlesFeed.update { myArticles }
            Result.Success(myArticles)

        }
    }

}
