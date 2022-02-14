package com.android.sample.app.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.sample.app.ui.Screens.Companion.LINK
import com.android.sample.app.ui.theme.ComposeTheme
import com.android.sample.app.util.AssetParamType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screens.Dashboard.title) {
                    composable(Screens.Dashboard.title) {
                        DashboardScreen(navController)
                    }
                    composable(
                        Screens.Section.title, arguments = listOf(
                            navArgument(LINK) {
                                type = AssetParamType()
                            }
                        )
                    ) {
                        SectionDetailsScreen(navController)
                    }
                }
            }
        }
    }
}
