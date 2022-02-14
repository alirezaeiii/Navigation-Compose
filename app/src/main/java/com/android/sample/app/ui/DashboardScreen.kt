package com.android.sample.app.ui

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.android.sample.app.R
import com.android.sample.app.ui.Screens.Companion.LINK
import com.android.sample.app.viewmodel.DashboardViewModel
import com.google.gson.Gson

@Composable
fun DashboardScreen(
    navController: NavHostController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = stringResource(id = R.string.label_dashboard))
                    }
                },
                elevation = 8.dp,
                modifier = Modifier.clip(
                    RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp)
                )
            )
        },
        content = {
            Content(viewModel = viewModel) { dashboard ->
                VerticalCollection(dashboard) { link ->
                    val json = Uri.encode(Gson().toJson(link))
                    navController.navigate(
                        Screens.Section.title.replace
                            ("{${LINK}}", json)
                    )
                }
            }
        })
}