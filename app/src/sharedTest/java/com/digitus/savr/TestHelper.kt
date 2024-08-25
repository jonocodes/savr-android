
package com.digitus.savr

import android.content.Context
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.digitus.savr.ui.SavrApp


fun ComposeContentTestRule.launchSavrApp(context: Context) {
    setContent {
        SavrApp(
            appContainer = TestAppContainer(context),
            widthSizeClass = WindowWidthSizeClass.Compact
        )
    }
}
