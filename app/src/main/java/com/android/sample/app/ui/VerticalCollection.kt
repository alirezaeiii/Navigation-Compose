package com.android.sample.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.sample.app.domain.Dashboard
import com.android.sample.app.domain.Link

@Composable
fun VerticalCollection(
    dashboard: Dashboard,
    onItemClick: (Link) -> Unit
) {
    LazyColumn {
        items(
            items = dashboard.links.sections,
            itemContent = { link ->
                VerticalListItem(link = link, onItemClick = onItemClick)
                ListItemDivider()
            }
        )
    }
}

@Composable
private fun VerticalListItem(
    link: Link,
    onItemClick: (Link) -> Unit
) {
    val typography = MaterialTheme.typography
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = { onItemClick(link) }),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = link.title,
            style = typography.h6
        )
    }
}

@Composable
private fun ListItemDivider() {
    Divider(
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
    )
}