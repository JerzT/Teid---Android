package com.example.musicapp.bottomBar

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.musicapp.R

@Composable
fun BottomBarPlayPauseButton(
    onClick: () -> (Unit),
) {
    val isPlaying: Boolean = true
    Button(
        onClick = { onClick() },
        contentPadding = PaddingValues(0.dp),
        elevation = null,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0x00FFFFFF),
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .size(64.dp),
    ){
        Icon(
            painter = painterResource(id = R.drawable.baseline_play_arrow_24),
            tint = MaterialTheme.colorScheme.surface,
            contentDescription = "play",
            modifier = Modifier
                .fillMaxSize(0.75F)
        )
    }
}