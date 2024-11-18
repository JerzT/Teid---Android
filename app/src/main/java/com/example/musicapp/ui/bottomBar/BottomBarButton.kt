package com.example.musicapp.ui.bottomBar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun BottomBarButton(
    onClick: () -> (Unit),
    painter: Painter,
    contentDescription: String,
) = Button(
    onClick = onClick,
    contentPadding = PaddingValues(0.dp),
    elevation = null,
    shape = CircleShape,
    colors = ButtonDefaults.buttonColors(
        containerColor = Color(0x00FFFFFF),
        contentColor = MaterialTheme.colorScheme.onSurface
    ),
    modifier = Modifier
        .size(52.dp),
){
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        tint = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxSize(0.75F)
    )
}