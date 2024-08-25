
package com.digitus.savr.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.digitus.savr.R
import com.digitus.savr.data.articles.impl.article1
import com.digitus.savr.model.Article
import com.digitus.savr.ui.theme.SavrTheme

@Composable
fun AuthorAndReadTime(
    article: Article,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text(
            text = stringResource(id = R.string.article_post_min_read).format(
                article.author,
                article.readTimeMinutes
            ),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ArticleImage(article: Article, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(article.imageThumbId),
        contentDescription = null, // decorative
        modifier = modifier
            .size(40.dp, 40.dp)
            .clip(MaterialTheme.shapes.small)
    )
}

@Composable
fun ArticleTitle(article: Article) {
    Text(
        text = article.title,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun ArticleCardSimple(
    article: Article,
    navigateToArticle: (String) -> Unit,
//    isFavorite: Boolean,
//    onToggleFavorite: () -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

//    val bookmarkAction = stringResource(if (isFavorite) R.string.unbookmark else R.string.bookmark)
    Row(
        modifier = Modifier
            .clickable(onClick = { navigateToArticle(article.slug) })
            .semantics {
                // By defining a custom action, we tell accessibility services that this whole
                // composable has an action attached to it. The accessibility service can choose
                // how to best communicate this action to the user.
//                customActions = listOf(
//                    CustomAccessibilityAction(
//                        label = bookmarkAction,
//                        action = { onToggleFavorite(); true }
//                    )
//                )
            }
    ) {
        ArticleImage(article, Modifier.padding(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 10.dp)
        ) {
            ArticleTitle(article)
            AuthorAndReadTime(article)
        }
        IconButton(onClick = { openDialog = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = stringResource(R.string.cd_more_actions)
            )
        }
//        BookmarkButton(
//            isBookmarked = isFavorite,
//            onClick = onToggleFavorite,
//            // Remove button semantics so action can be handled at row level
//            modifier = Modifier
//                .clearAndSetSemantics {}
//                .padding(vertical = 2.dp, horizontal = 6.dp)
//        )
    }
}

@Composable
fun ArticleCardHistory(article: Article, navigateToArticle: (String) -> Unit) {
    var openDialog by remember { mutableStateOf(false) }

    Row(
        Modifier
            .clickable(onClick = { navigateToArticle(article.slug) })
    ) {
        ArticleImage(
            article = article,
            modifier = Modifier.padding(16.dp)
        )
        Column(
            Modifier
                .weight(1f)
                .padding(vertical = 12.dp)
        ) {
//            Text(
//                text = stringResource(id = R.string.home_article_based_on_history),
//                style = MaterialTheme.typography.labelMedium
//            )
            ArticleTitle(article = article)
            AuthorAndReadTime(
                article = article,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        IconButton(onClick = { openDialog = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = stringResource(R.string.cd_more_actions)
            )
        }
    }
    if (openDialog) {
        AlertDialog(
            modifier = Modifier.padding(20.dp),
            onDismissRequest = { openDialog = false },
            title = {
                Text(
                    text = stringResource(id = R.string.fewer_stories),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.fewer_stories_content),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Text(
                    text = stringResource(id = R.string.agree),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(15.dp)
                        .clickable { openDialog = false }
                )
            }
        )
    }
}

//@Preview("Bookmark Button")
//@Composable
//fun BookmarkButtonPreview() {
//    JetnewsTheme {
//        Surface {
//            BookmarkButton(isBookmarked = false, onClick = { })
//        }
//    }
//}
//
//@Preview("Bookmark Button Bookmarked")
//@Composable
//fun BookmarkButtonBookmarkedPreview() {
//    JetnewsTheme {
//        Surface {
//            BookmarkButton(isBookmarked = true, onClick = { })
//        }
//    }
//}

@Preview("Simple Article card")
@Preview("Simple Article card (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SimpleArticlePreview() {
    SavrTheme {
        Surface {
            ArticleCardSimple(article1, {})
        }
    }
}

@Preview("Article History card")
@Composable
fun HistoryArticlePreview() {
    SavrTheme {
        Surface {
            ArticleCardHistory(article1, {})
        }
    }
}
