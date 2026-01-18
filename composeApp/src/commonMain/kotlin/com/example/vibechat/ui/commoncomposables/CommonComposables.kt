package com.example.vibechat.ui.commoncomposables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    text: String,
    fontSize: TextUnit = 12.sp,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    Text(
        text = text,
        color = color,
        modifier = modifier,
        fontSize = fontSize,
        fontWeight = fontWeight
    )
}
val whiteColor = Color.White

@Composable
expect fun LoadNetworkImage(url: String ,modifier: Modifier)