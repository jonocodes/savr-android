
package com.digitus.savr

import android.content.Context
import com.digitus.savr.data.AppContainer
import com.digitus.savr.data.articles.ArticlesRepository
import com.digitus.savr.data.articles.impl.BlockingFakeArticlesRepository

class TestAppContainer(private val context: Context) : AppContainer {

    override val articlesRepository: ArticlesRepository by lazy {
        BlockingFakeArticlesRepository()
    }

}
