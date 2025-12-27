package com.example.vibechat.ui.commoncomposables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage

@Composable
actual fun LoadNetworkImage(url: String, modifier: Modifier) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = modifier
    )
}