package com.android.sample.app.ui.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.android.sample.app.ui.theme.ComposeTheme

@Composable
fun DetailTitleText(resourceId: Int) {
    Text(
        text = stringResource(id = resourceId),
        fontWeight = FontWeight.Bold,
        fontSize = ComposeTheme.fontSizes.sp_18
    )
}

@Composable
fun DetailDescriptionText(text: String) {
    Text(
        text = text,
        fontSize = ComposeTheme.fontSizes.sp_16
    )
}