package com.android.sample.app.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.navArgument
import com.android.sample.app.ui.Screens.Companion.LINK
import com.android.sample.app.ui.theme.ComposeTheme
import com.android.sample.app.util.LinkNavType
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                val navController = rememberAnimatedNavController()
                AnimatedNavHost(navController = navController,
                    startDestination = Screens.Dashboard.title,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }) {
                    composable(Screens.Dashboard.title) {
                        DashboardScreen(navController)
                    }
                    composable(
                        Screens.Section.title, arguments = listOf(
                            navArgument(LINK) {
                                type = LinkNavType()
                            }
                        )
                    ) {
                        SectionScreen(navController)
                    }
                }
            }
        }
    }
}
