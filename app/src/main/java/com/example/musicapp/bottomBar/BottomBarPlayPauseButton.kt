package com.example.musicapp.bottomBar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.contextaware.ContextAware
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.musicapp.R
import java.nio.file.Paths

@SuppressLint("SdCardPath")
@Composable
fun BottomBarPlayPauseButton(

) {
    val isPlaying: MutableState<Boolean> = remember{ mutableStateOf(false) }
    val mediaPlayer = MediaPlayer.create(LocalContext.current, R.raw.dealer)

    Button(
        onClick = {
            onClick(
                mediaPlayer = mediaPlayer,
                isPlaying = isPlaying,
            )},
        contentPadding = PaddingValues(0.dp),
        elevation = null,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .size(64.dp),
    ){
        val painter: Painter
        val contentDescription: String
        if (isPlaying.value){
            painter = painterResource(id = R.drawable.baseline_pause_24)
            contentDescription = "pause"

        } else{
            painter = painterResource(id = R.drawable.baseline_play_arrow_24)
            contentDescription = "play"
        }
        Icon(
            painter = painter,
            tint = MaterialTheme.colorScheme.surface,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize(0.85F)
        )
    }
}

fun onClick(
    mediaPlayer: MediaPlayer,
    isPlaying: MutableState<Boolean>,
){
    if (isPlaying.value){
        mediaPlayer.pause()
    }
    else{
        mediaPlayer.start()
    }
    isPlaying.value = !isPlaying.value
}
