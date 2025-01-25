package com.example.musicapp.ui.bottomBar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.navigation.NavController
import com.example.musicapp.R
import com.example.musicapp.logic.mediaPlayer.AppExoPlayer
import com.example.musicapp.ui.actuallyPlaying.ActuallyPlayingBar

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun BottomBarCustom(
    albumList: List<Any>,
    navController: NavController,
    songListUri: List<String>? = null,
){
    val loopStatus = remember { mutableIntStateOf(AppExoPlayer.stateOfLoop.intValue) }
    val shuffleStatus = remember { mutableStateOf(AppExoPlayer.stateOfShuffle.value) }

    LaunchedEffect(AppExoPlayer.stateOfLoop.intValue) {
        loopStatus.intValue = AppExoPlayer.stateOfLoop.intValue
    }

    LaunchedEffect(AppExoPlayer.stateOfShuffle.value) {
        shuffleStatus.value = AppExoPlayer.stateOfShuffle.value
    }

    BottomAppBar(
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ActuallyPlayingBar(
                albumList = albumList,
                navController = navController,
                songListUri = songListUri,
                modifier = Modifier
                    .weight(1f)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(vertical = 10.dp)

            ) {
                //shuffle
                BottomBarButton(
                    onClick = {
                        AppExoPlayer.shufflePlaylist()
                    },
                    painter = if(shuffleStatus.value) painterResource(id = R.drawable.baseline_shuffle_24)
                    else painterResource(id = R.drawable.baseline_no_shuffle_24),
                    contentDescription = "Shuffle"
                )
                //previous
                BottomBarButton(
                    onClick = {
                        AppExoPlayer.previousSong()
                    },
                    painter = painterResource(id = R.drawable.baseline_skip_previous_24),
                    contentDescription = "Previous song"
                )

                BottomBarPlayPauseButton()

                //next
                BottomBarButton(
                    onClick = {
                        AppExoPlayer.nextSong()
                    },
                    painter = painterResource(id = R.drawable.baseline_skip_next_24),
                    contentDescription = "Next song"
                )
                //loop
                BottomBarButton(
                    onClick = { handleLooping() },
                    painter = when(loopStatus.intValue){
                        Player.REPEAT_MODE_OFF -> painterResource(R.drawable.baseline_no_replay_24)
                        Player.REPEAT_MODE_ONE -> painterResource(R.drawable.baseline_replay_24)
                        Player.REPEAT_MODE_ALL -> painterResource(R.drawable.baseline_loop_24)
                        else -> painterResource(R.drawable.baseline_no_replay_24) },
                    contentDescription = "Play"
                )
            }
        }
    }
}