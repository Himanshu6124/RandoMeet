package com.example.vibechat.ui.commoncomposables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun HorizontalSpacer( width : Dp){
    Spacer(modifier = Modifier.width(width))
}

@Composable
fun VerticalSpacer( height : Dp){
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun TextComposable(
    text: String,
    fontSize: TextUnit = 12.sp,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight
    )
}

@Composable
expect fun LoadNetworkImage(url: String ,modifier: Modifier)