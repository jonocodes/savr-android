
package com.digitus.savr.ui.article

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.digitus.savr.SavrApplication.Companion.appSavesDir
import com.digitus.savr.R
import com.digitus.savr.data.articles.impl.article1
import com.digitus.savr.model.Article
import com.digitus.savr.ui.theme.SavrTheme
import com.digitus.savr.ui.utils.BookmarkButton
import com.digitus.savr.ui.utils.FavoriteButton
import com.digitus.savr.ui.utils.LOGTAG
import com.digitus.savr.ui.utils.ShareButton
import com.digitus.savr.ui.utils.TextSettingsButton
import com.digitus.savr.ui.utils.readTextFromUri

/**
 * Stateless Article Screen that displays a single post adapting the UI to different screen sizes.
 *
 * @param showNavigationIcon (state) if the navigation icon should be shown
 * @param onBack (event) request navigate back
 * @param isFavorite (state) is this item currently a favorite
 * @param onToggleFavorite (event) request that this post toggle it's favorite state
 * @param lazyListState (state) the [LazyListState] for the Article content
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    article: Article,
    isExpandedScreen: Boolean,
    onBack: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    var showUnimplementedActionDialog by rememberSaveable { mutableStateOf(false) }
    if (showUnimplementedActionDialog) {
        FunctionalityNotAvailablePopup { showUnimplementedActionDialog = false }
    }

    Row(modifier.fillMaxSize()) {
        val context = LocalContext.current
        ArticleScreenContent(
            article = article,
            // Allow opening the Drawer if the screen is not expanded
            navigationIconContent = {
                if (!isExpandedScreen) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_up),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            // Show the bottom bar if the screen is not expanded
            bottomBarContent = {
                if (!isExpandedScreen) {
                    BottomAppBar(
                        actions = {
                            FavoriteButton(onClick = { showUnimplementedActionDialog = true })
                            BookmarkButton(isBookmarked = isFavorite, onClick = onToggleFavorite)
                            ShareButton(onClick = { shareArticle(article, context) })
                            TextSettingsButton(onClick = { showUnimplementedActionDialog = true })
                        }
                    )
                }
            },
            lazyListState = lazyListState
        )
    }
}



@ExperimentalMaterial3Api
@Composable
fun DisplayWebView(article: Article) {
    val view = WebView(LocalContext.current).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // NOTE: these break the preview renderer
        settings.allowFileAccess = true
        settings.allowContentAccess = true
    }

    view.visibility = VISIBLE

    AndroidView(factory = {view})

//                view.webChromeClient = object : WebChromeClient() {
//
//                    override fun onConsoleMessage(message: ConsoleMessage): Boolean {
//                        Log.d("MyApplication JS", "${message.message()} -- From line " +
//                                "${message.lineNumber()} of ${message.sourceId()}")
//                        return true
//                    }
//                }


    val articleDir = appSavesDir?.findFile(article.slug)

    val readabilityFile = articleDir?.findFile("index.html")

    var html = """
                        <h2>Content not found</h2><br />
                        <img src="test.png">
                        """

    if (readabilityFile != null) {
        html = readTextFromUri(LocalContext.current, readabilityFile.uri)
    }

    Log.d(LOGTAG, html)

    view.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null)
}

/**
 * Stateless Article Screen that displays a single post.
 *
 * @param navigationIconContent (UI) content to show for the navigation icon
 * @param bottomBarContent (UI) content to show for the bottom bar
 */
@ExperimentalMaterial3Api
@Composable
private fun ArticleScreenContent(
    article: Article,
    navigationIconContent: @Composable () -> Unit = { },
    bottomBarContent: @Composable () -> Unit = { },
    lazyListState: LazyListState = rememberLazyListState()
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    Scaffold(
        topBar = {
            ArticleTopAppBar(
                title = "pub name", // article.publication?.name.orEmpty(),
                navigationIconContent = navigationIconContent,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = bottomBarContent
    ) {

//        innerPadding -> DisplayWebView()

//        innerPadding -> BodyContent(Modifier.padding(innerPadding))

        innerPadding -> Column(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = innerPadding.calculateBottomPadding(), top = 100.dp)
        .verticalScroll(rememberScrollState())) {
            DisplayWebView(article)
        }


//        LazyColumn(
//            contentPadding = innerPadding,
////            modifier = modifier.padding(horizontal = defaultSpacerSize),
//            state = lazyListState,
//        ) {
//            DisplayWebView()
//        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticleTopAppBar(
    title: String,
    navigationIconContent: @Composable () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    TopAppBar(
        title = {
            Text("article",

                maxLines = 1,
                overflow = TextOverflow.Ellipsis
                )
//            Row {
//                Image(
//                    painter = painterResource(id = R.drawable.icon_article_background),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .clip(CircleShape)
//                        .size(36.dp)
//                )
////                Text(
////                    text = stringResource(R.string.published_in, title),
////                    style = MaterialTheme.typography.labelLarge,
////                    modifier = Modifier.padding(start = 8.dp)
////                )
//            }
        },
        actions = {

            IconButton(onClick = {
//                Toast.makeText(
//                    context,
//                    "Archive view is not yet implemented",
//                    Toast.LENGTH_LONG
//                ).show()

            }) {
                Icon(
                    imageVector = Icons.Filled.PermMedia,
                    contentDescription = "Saves"
                )
            }

//            IconButton(onClick = {
//                Toast.makeText(
//                    context,
//                    "Archive view is not yet implemented",
//                    Toast.LENGTH_LONG
//                ).show()
//
//            }
//            )

        },
        navigationIcon = navigationIconContent,
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

/**
 * Display a popup explaining functionality not available.
 *
 * @param onDismiss (event) request the popup be dismissed
 */
@Composable
private fun FunctionalityNotAvailablePopup(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = stringResource(id = R.string.post_functionality_not_available),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.close))
            }
        }
    )
}

/**
 * Show a share sheet for a post
 *
 * @param post to share
 * @param context Android context to show the share sheet in
 */
fun shareArticle(post: Article, context: Context) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, post.title)
        putExtra(Intent.EXTRA_TEXT, post.url)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.post_share_post)
        )
    )
}

@Preview("Article screen navrail", device = Devices.PIXEL_C)
@Composable
fun JonosPreview() {
    SavrTheme {
//        val article = runBlocking {
//            (BlockingFakeArticlesRepository().getArticle("first-slug") as Result.Success).data
//        }
        ArticleScreen(article1, true, {}, false, {})
    }
}


//
//@Preview("Article screen navrail", device = Devices.PIXEL_C)
//@Preview(
//    "Article screen navrail (dark)",
//    uiMode = UI_MODE_NIGHT_YES,
//    device = Devices.PIXEL_C
//)
//@Preview("Article screen navrail (big font)", fontScale = 1.5f, device = Devices.PIXEL_C)
//@Composable
//fun PreviewArticleNavRail() {
//    JetnewsTheme {
//        val article = runBlocking {
//            (BlockingFakeArticlesRepository().getArticle("first-slug") as Result.Success).data
//        }
//        ArticleScreen(article, true, {}, false, {})
//    }
//}
