//package com.example.musicapp.ui.bottomBar
//
//import android.annotation.SuppressLint
//import androidx.annotation.OptIn
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.painter.Painter
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.media3.common.Player
//import androidx.media3.common.util.UnstableApi
//import com.example.musicapp.App
//import com.example.musicapp.R
//import com.example.musicapp.logic.mediaPlayer.AppExoPlayer
//
//@OptIn(UnstableApi::class)
//@SuppressLint("SdCardPath")
//@Composable
//fun BottomBarPlayPauseButton() {
//    val isPlaying = remember { mutableStateOf(false) }
//    val context = LocalContext.current
//
//    AppExoPlayer.player?.addListener(object : Player.Listener{
//        @Deprecated("Deprecated in Java", ReplaceWith("isPlaying.value = playWhenReady"))
//        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//            isPlaying.value = playWhenReady
//        }
//    })
//
//    LaunchedEffect(Unit) {
//        isPlaying.value = AppExoPlayer.player?.isPlaying ?: false
//    }
//    Button(
//        onClick = {
//            if (isPlaying.value){
//                AppExoPlayer.stopMusic()
//            }
//            else{
//                AppExoPlayer.playMusic()
//            }
//        },
//        contentPadding = PaddingValues(0.dp),
//        elevation = null,
//        shape = CircleShape,
//        colors = ButtonDefaults.buttonColors(
//            containerColor = MaterialTheme.colorScheme.tertiary,
//            contentColor = MaterialTheme.colorScheme.onSurface
//        ),
//        modifier = Modifier
//            .size(64.dp),
//    ){
//        val painter: Painter
//        val contentDescription: String
//
//        if (isPlaying.value){
//            painter = painterResource(id = R.drawable.baseline_pause_24)
//            contentDescription = "pause"
//
//        } else{
//            painter = painterResource(id = R.drawable.baseline_play_arrow_24)
//            contentDescription = "play"
//        }
//
//        Icon(
//            painter = painter,
//            tint = MaterialTheme.colorScheme.surface,
//            contentDescription = contentDescription,
//            modifier = Modifier
//                .fillMaxSize(0.85F)
//        )
//    }
//}