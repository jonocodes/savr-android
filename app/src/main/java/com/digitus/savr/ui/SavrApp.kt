
package com.digitus.savr.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.digitus.savr.data.AppContainer
import com.digitus.savr.ui.theme.SavrTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavrApp(
    appContainer: AppContainer,
    widthSizeClass: WindowWidthSizeClass,
) {
    SavrTheme {
        val navController = rememberNavController()

        val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

            Row {
                if (isExpandedScreen) {
//                    AppNavRail(
//                        currentRoute = currentRoute,
//                        navigateToHome = navigationActions.navigateToHome,
//                        navigateToInterests = navigationActions.navigateToInterests,
//                    )
                }
                SavrNavGraph(
                    appContainer = appContainer,
                    isExpandedScreen = isExpandedScreen,
                    navController = navController,
                )
            }
//        }
    }
}
