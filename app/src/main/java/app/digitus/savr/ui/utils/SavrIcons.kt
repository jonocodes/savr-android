
package app.digitus.savr.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import app.digitus.savr.R

//@Composable
//fun FavoriteButton(onClick: () -> Unit) {
//    IconButton(onClick) {
//        Icon(
//            imageVector = Icons.Filled.Headset,
////            contentDescription = stringResource(R.string.cd_add_to_favorites)
//            contentDescription = "Listen to Article"
//        )
//    }
//}
//
//@Composable
//fun BookmarkButton(
//    isBookmarked: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val clickLabel = stringResource(
//        if (isBookmarked) R.string.unbookmark else R.string.bookmark
//    )
//    IconToggleButton(
//        checked = isBookmarked,
//        onCheckedChange = { onClick() },
//        modifier = modifier.semantics {
//            // Use a custom click label that accessibility services can communicate to the user.
//            // We only want to override the label, not the actual action, so for the action we pass null.
//            this.onClick(label = clickLabel, action = null)
//        }
//    ) {
//        Icon(
//            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
//            contentDescription = null // handled by click label of parent
//        )
//    }
//}

@Composable
fun ShareButton(onClick: () -> Unit) {
    IconButton(onClick) {
        Icon(
            imageVector = Icons.Filled.Share,
            contentDescription = stringResource(R.string.cd_share)
        )
    }
}

@Composable
fun TextSettingsButton(onClick: () -> Unit) {
    IconButton(onClick) {
        Icon(
            painter = painterResource(R.drawable.ic_text_settings),
            contentDescription = stringResource(R.string.cd_text_settings)
        )
    }
}
