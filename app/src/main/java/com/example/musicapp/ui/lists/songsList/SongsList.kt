package com.example.musicapp.ui.lists.songsList

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.musicapp.logic.song.Song
import com.example.musicapp.logic.song.getSongs
import com.example.musicapp.logic.song.getSongsFromDatabaseWithUri

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun SongsList(
    listUri: List<String>,
    searchText: MutableState<String>,
    //album list is needed to get images to notification
    albumsList: List<Any>,
){
    val context = LocalContext.current

    val discList = remember { mutableStateListOf<MutableList<Song>>()}

    LaunchedEffect(listUri) {
        if (listUri.isNotEmpty()){
            for (i in 0..< listUri.count()){
                discList.add(mutableStateListOf())
                discList[i].addAll(getSongsFromDatabaseWithUri(context, listUri[i].toUri()))
                discList[i].sortBy { song -> song.number }
            }
            for (i in 0..< listUri.count()){
                val songsFromDirectory: SnapshotStateList<Song> = mutableStateListOf()
                getSongs(
                    uri = listUri[i].toUri(),
                    context = context,
                    songsList = songsFromDirectory
                ).await()
                discList[i].clear()
                discList[i].addAll(songsFromDirectory)
                discList[i].sortBy { song -> song.number }
            }
        }
    }

    val filteredDisc = discList.map { disc ->
        disc.filter { song: Song ->
            song.title!!.contains(searchText.value, ignoreCase = true)
        }.sortedBy { it.number }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        for (disc in filteredDisc){
            DiscInfoHeader(
                songsList = disc,
                discIndex = filteredDisc.indexOf(disc)
            )
            HeaderOfDisc()
            HorizontalDivider(
                color = MaterialTheme.colorScheme.surface,
                thickness = 2.dp
            )
            for (song in disc){
                SongItem(
                    song = song,
                    songsList = disc,
                    albumsList = albumsList,
                )
            }
        }
    }
}