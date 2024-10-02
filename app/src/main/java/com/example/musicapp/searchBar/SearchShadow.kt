package com.example.musicapp.searchBar

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchShadow(){
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .drawBehind {
                val shadowHeight = 40.dp.toPx()
                val gradientColors = listOf(
                    Color.Black.copy(alpha = 0.20f),
                    Color.Transparent
                )
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = gradientColors,
                        startY = size.height,
                        endY = size.height + shadowHeight
                    ),
                    topLeft = Offset(0f, size.height),
                    size = Size(size.width, shadowHeight)
                )
            }
    )
}