package com.example.musicapp.ui.bottomBar

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.musicapp.R
import com.example.musicapp.logic.album.Album
import com.example.musicapp.logic.mediaPlayer.MediaPlayerApp
import com.example.musicapp.ui.actuallyPlaying.ActuallyPlayingBar

@Composable
fun BottomBarCustom(
    albumList: List<Album>,
){
    val context = LocalContext.current

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
                        MediaPlayerApp.shuffleSongList()
                    },
                    painter = painterResource(id = R.drawable.baseline_shuffle_24),
                    contentDescription = "Play"
                )
                //previous
                BottomBarButton(
                    onClick = {
                        MediaPlayerApp.previousSongPlay(
                            context = context
                        )
                    },
                    painter = painterResource(id = R.drawable.baseline_skip_previous_24),
                    contentDescription = "Play"
                )

                BottomBarPlayPauseButton()

                //next
                BottomBarButton(
                    onClick = {
                        MediaPlayerApp.nextSongPlay(
                            context = context
                        )
                    },
                    painter = painterResource(id = R.drawable.baseline_skip_next_24),
                    contentDescription = "Play"
                )
                //loop
                BottomBarButton(
                    onClick = { /*TODO*/ },
                    painter = painterResource(id = R.drawable.baseline_replay_24),
                    contentDescription = "Play"
                )
            }
        }
    }
}