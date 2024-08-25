
package com.digitus.savr.data.articles

import com.digitus.savr.data.Result
import com.digitus.savr.model.Article
import com.digitus.savr.model.ArticlesFeed

/**
 * Interface to the Articles data layer.
 */
interface ArticlesRepository {

    suspend fun getArticle(articleSlug: String?): Result<Article>

    suspend fun getArticlesFeed(): Result<ArticlesFeed>

    /**
     * Observe the posts feed.
     */
//    fun observeArticlesFeed(): Flow<ArticlesFeed?>

}
