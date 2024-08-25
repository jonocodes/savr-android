package com.digitus.savr.data

import android.content.Context
import com.digitus.savr.data.articles.ArticlesRepository
import com.digitus.savr.data.articles.impl.SavedArticlesRepository

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val articlesRepository: ArticlesRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class AppContainerImpl(val applicationContext: Context) : AppContainer {

    override val articlesRepository: ArticlesRepository by lazy {
        SavedArticlesRepository()
//        DirectoryArticlesRepository()
    }

}
