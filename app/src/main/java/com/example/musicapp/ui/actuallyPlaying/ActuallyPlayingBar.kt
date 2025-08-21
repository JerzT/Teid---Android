//package com.example.musicapp.ui.actuallyPlaying
//
//import android.annotation.SuppressLint
//import android.util.Log
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Slider
//import androidx.compose.material.SliderDefaults
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableFloatStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.graphics.painter.BitmapPainter
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.arjunjadeja.texty.DisplayStyle
//import com.arjunjadeja.texty.Texty
//import com.example.musicapp.R
//import com.example.musicapp.Screen
//import com.example.musicapp.newLogic.album.Album
//import com.example.musicapp.logic.image.albumCoverCache
//import com.example.musicapp.logic.mediaPlayer.AppExoPlayer
//import com.example.musicapp.logic.song.Song
//import kotlinx.coroutines.delay
//
//@SuppressLint("DefaultLocale", "RestrictedApi")
//@Composable
//fun ActuallyPlayingBar(
//    albumList: List<Any>,
//    navController: NavController,
//    songListUri: List<String>?,
//    modifier: Modifier,
//) {
//    val image = remember { mutableStateOf<ImageBitmap?>(null) }
//    val progress = remember { mutableFloatStateOf(0f) }
//    val durationText = remember { mutableStateOf("0:00") }
//    val currentPositionText = remember { mutableStateOf("0:00") }
//    val currentSong = remember { AppExoPlayer.currentSong }
//    val isPlaying = remember { AppExoPlayer.isPlaying }
//
//    // Getting the currently played album and image.
//    var actuallyPlayedAlbum = remember(currentSong.value) {
//        currentSong.value?.let { getActuallyPlayedAlbum(albumList, it) }
//    }
//
//    val screenWidth = LocalDensity.current.run { LocalConfiguration.current.screenWidthDp.dp.toPx() }
//    val titleWidth = with(LocalDensity.current) { (currentSong.value?.title?.length?.times(40))?.toDp()?.toPx() }
//
//    LaunchedEffect(currentSong.value) {
//        currentSong.value?.let { song ->
//            actuallyPlayedAlbum = getActuallyPlayedAlbum(albumList, song)
//            image.value = getImageFromAlbum(actuallyPlayedAlbum!!, song)
//            progress.floatValue = AppExoPlayer.player!!.currentPosition.toFloat() / AppExoPlayer.player!!.duration.toFloat()
//
//            // Duration formatting
//            val durationMinutes = song.length / 1000 / 60
//            val durationSeconds = song.length / 1000 % 60
//            durationText.value = String.format("%d:%02d", durationMinutes, durationSeconds)
//        }
//    }
//
//    LaunchedEffect(isPlaying.value) {
//        while (isPlaying.value) {
//            progress.floatValue = AppExoPlayer.player!!.currentPosition.toFloat() / AppExoPlayer.player!!.duration.toFloat()
//            delay(100)
//        }
//    }
//
//    LaunchedEffect(progress.floatValue) {
//        val currentMinutes = AppExoPlayer.player?.currentPosition!! / 1000 / 60
//        val currentSeconds = AppExoPlayer.player?.currentPosition!! / 1000 % 60
//        currentPositionText.value = String.format("%d:%02d", currentMinutes, currentSeconds)
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//            .fillMaxHeight()
//            .background(MaterialTheme.colorScheme.onBackground)
//            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
//    ) {
//        Row(
//            modifier = Modifier
//                .weight(5f)
//                .padding(10.dp, 0.dp, 10.dp, 10.dp)
//                .offset(y = 10.dp)
//        ) {
//            Button(
//                onClick = {
//                    when (val album = actuallyPlayedAlbum) {
//                        is Album -> {
//                            if(songListUri?.get(0) == album.uri.toString()){
//                                return@Button
//                            }
//                            navController.navigate(
//                                Screen.SongList(
//                                    listUri = listOf(album.uri.toString()),
//                                    name = album.name.toString()
//                                )
//                            )
//                        }
//                        is List<*> -> {
//                            for(i in 0..(album as List<Album>).count()){
//                                if (songListUri?.get(i) == album[i].uri.toString()){
//                                    return@Button
//                                }
//                            }
//                            val sortedUris = album.sortedBy { it.cdNumber }
//                                .map { it.uri.toString() }.toMutableList()
//                            navController.navigate(
//                                Screen.SongList(
//                                    listUri = sortedUris,
//                                    name = album[0].name.toString()
//                                )
//                            )
//                        }
//                    }
//                },
//                elevation = null,
//                shape = RoundedCornerShape(4.dp),
//                contentPadding = PaddingValues(0.dp)
//            ) {
//                Image(
//                    painter = image.value?.let { BitmapPainter(it) }
//                        ?: painterResource(id = R.drawable.baseline_question_mark_24),
//                    contentScale = ContentScale.Crop,
//                    contentDescription = "Album Cover",
//                    colorFilter = if (image.value == null) {
//                        if (isSystemInDarkTheme()) ColorFilter.lighting(Color.Black, Color.White)
//                        else null
//                    } else null,
//                    modifier = Modifier
//                        .width(104.dp)
//                        .fillMaxHeight()
//                        .clip(RoundedCornerShape(4.dp))
//                        .border(
//                            width = 1.dp,
//                            color = MaterialTheme.colorScheme.surface,
//                            shape = RoundedCornerShape(4.dp)
//                        )
//                )
//            }
//
//            Column(
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier
//                    .padding(10.dp)
//                    .fillMaxHeight()
//            ) {
//                currentSong.value?.let {
//                    Log.v("test2", "$titleWidth, $screenWidth")
//                    if(titleWidth!! > screenWidth){
//                        Texty(
//                            text = it.title ?: "",
//                            textStyle = TextStyle(
//                                color = MaterialTheme.colorScheme.surface
//                            ),
//                            displayStyle = DisplayStyle.Sliding(
//                                duration = titleWidth.toLong() * 7,
//
//                            ),
//                            maxLines = 1
//                        )
//                    }
//                    else{
//                        Text(
//                            color = MaterialTheme.colorScheme.surface,
//                            text = it.title ?: "",
//                            fontSize = 26.sp,
//                            fontWeight = FontWeight(900),
//                            maxLines = 1
//                        )
//                    }
//
//                    Text(
//                        color = MaterialTheme.colorScheme.surface,
//                        text = it.author ?: "",
//                        maxLines = 1
//                    )
//                }
//            }
//        }
//
//        Box(
//            modifier = Modifier
//                .padding(horizontal = 10.dp)
//                .weight(2f)
//        ) {
//            Row(
//                horizontalArrangement = Arrangement.End,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .offset(y = (-10).dp)
//            ) {
//                Text(
//                    text = currentPositionText.value,
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//                Text(
//                    text = " / " + durationText.value,
//                    color = MaterialTheme.colorScheme.surface
//                )
//            }
//
//            Slider(
//                value = progress.floatValue,
//                onValueChange = { value ->
//                    progress.floatValue = value
//                    val validTime = (value * AppExoPlayer.player?.duration!!).toInt()
//                    AppExoPlayer.player?.seekTo(validTime.toLong())
//                },
//                modifier = Modifier.fillMaxWidth(),
//                colors = SliderDefaults.colors(
//                    thumbColor = MaterialTheme.colorScheme.tertiary,
//                    activeTrackColor = MaterialTheme.colorScheme.tertiary,
//                    inactiveTrackColor = MaterialTheme.colorScheme.background
//                )
//            )
//        }
//    }
//}
//
//private fun getImageFromAlbum(
//    album: Any,
//    currentPlaying: Song,
//): ImageBitmap? {
//    return when (album) {
//        is Album -> {
//            if (album.uri == currentPlaying.parentUri) {
//                albumCoverCache["${album.uri}"]
//            } else null
//        }
//        is List<*> -> {
//            (album as List<Album>).firstOrNull { it.uri == currentPlaying.parentUri }
//                ?.let { albumCoverCache["${it.uri}"] }
//        }
//        else -> null
//    }
//}
//
//private fun getActuallyPlayedAlbum(
//    albumList: List<Any>,
//    currentPlaying: Song,
//): Any? {
//    return albumList.firstOrNull { album ->
//        when (album) {
//            is Album -> album.uri == currentPlaying.parentUri
//            is List<*> -> (album as List<Album>).any { it.uri == currentPlaying.parentUri }
//            else -> false
//        }
//    }
//}
