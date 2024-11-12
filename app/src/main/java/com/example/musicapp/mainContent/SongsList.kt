package com.example.musicapp.mainContent

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.example.musicapp.R
import com.example.musicapp.musicFilesUsage.MediaPlayerApp
import com.example.musicapp.musicFilesUsage.Song
import com.example.musicapp.musicFilesUsage.getSongs
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun SongsList(
    uri: Uri?,
){
    val context = LocalContext.current

    val songsList = remember{ mutableStateListOf<Song>() }

    val sortedSongsList = remember(songsList){
        derivedStateOf {
            songsList.sortedBy { song -> song.number }
        }
    }

    LaunchedEffect(uri) {
        if (uri != null){
            getSongs(
                uri = uri,
                context = context,
                songsList = songsList
            ).await()
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        HeaderOfDisc()
        HorizontalDivider(
            color = MaterialTheme.colorScheme.surface,
            thickness = 2.dp
        )
        for (song in sortedSongsList.value){
            SongItem(song = song)
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun SongItem(
    song: Song,
){
    val context = LocalContext.current

    val minutes = (song.length / 1000) / 60
    val seconds = (song.length / 1000) % 60

    val songDurationFormated = String.format("%d:%02d", minutes, seconds)

    Button(
        contentPadding = PaddingValues(5.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.tertiary,
            containerColor = MaterialTheme.colorScheme.background,
        ),
        onClick = {
            testPlaying(
                context = context,
                uri = song.uri,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = song.number.toString(),
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = song.title ?: "",
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(9f)

            )
            Text(
                text = song.timePlayed.toString(),
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = songDurationFormated,
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(3f)
            )
        }
    }
}

@Composable
private fun HeaderOfDisc(){
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 0.dp)
    ){
        Text(
            text = "#",
            color = MaterialTheme.colorScheme.surface,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = "Title:",
            color = MaterialTheme.colorScheme.surface,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(8f)

        )
        Text(
            text = "Play Count:",
            color = MaterialTheme.colorScheme.surface,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(7f)
        )
        Icon(
            painter = painterResource(id = R.drawable.baseline_clock_24),
            contentDescription = "Time",
            tint = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .weight(1f)
        )
    }
}

private fun testPlaying(
    context: Context,
    uri: Uri
){
    MediaPlayerApp.addMusicToPlay(
        context = context,
        uri = uri
    )
    MediaPlayerApp.playMusic()
}