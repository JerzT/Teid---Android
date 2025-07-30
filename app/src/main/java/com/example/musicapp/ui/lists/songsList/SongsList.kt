//package com.example.musicapp.ui.lists.songsList
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonColors
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.snapshots.SnapshotStateList
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.core.net.toUri
//import com.example.musicapp.logic.mediaPlayer.AppExoPlayer
//import com.example.musicapp.logic.song.Song
//import com.example.musicapp.logic.song.getSongs
//import com.example.musicapp.logic.song.getSongsFromDatabaseWithUri
//import com.example.musicapp.logic.song.synchronizeSongs
//
//@RequiresApi(Build.VERSION_CODES.P)
//@Composable
//fun SongsList(
//    listUri: List<String>,
//    searchText: MutableState<String>,
//    //album list is needed to get images to notification
//    albumsList: List<Any>,
//){
//    val context = LocalContext.current
//
//    val discList = remember { mutableStateListOf<MutableList<Song>>()}
//    val firstSongFromList = remember { mutableStateOf<Song?>(null) }
//
//    LaunchedEffect(listUri) {
//        if (listUri.isNotEmpty()){
//            val songsFromDatabaseList: MutableList<MutableList<Song>> = mutableListOf()
//            val songsFromDirectoryList: MutableList<MutableList<Song>> = mutableListOf()
//
//            for (i in 0..< listUri.count()){
//                val songsFromDatabase = getSongsFromDatabaseWithUri(context, listUri[i].toUri())
//                songsFromDatabaseList.add(songsFromDatabase)
//                discList.add(mutableStateListOf())
//                discList[i].addAll(songsFromDatabase)
//                discList[i].sortBy { song -> song.number }
//            }
//            if (discList[0].isNotEmpty()){
//                firstSongFromList.value = discList[0][0]
//            }
//
//            for (i in 0..< listUri.count()){
//                val songsFromDirectory: SnapshotStateList<Song> = mutableStateListOf()
//                getSongs(
//                    uri = listUri[i].toUri(),
//                    context = context,
//                    songsList = songsFromDirectory
//                ).await()
//                songsFromDirectoryList.add(songsFromDirectory)
//                discList[i].clear()
//                discList[i].addAll(songsFromDirectory)
//                discList[i].sortBy { song -> song.number }
//            }
//            if (discList[0].isNotEmpty()){
//                firstSongFromList.value = discList[0][0]
//            }
//            synchronizeSongs(
//                songsFromDatabase = songsFromDatabaseList.flatten().toMutableList(),
//                songsInDirectory = songsFromDirectoryList.flatten().toMutableList(),
//                context = context
//            )
//        }
//    }
//
//    val filteredDisc = discList.map { disc ->
//        disc.filter { song: Song ->
//            song.title!!.contains(searchText.value, ignoreCase = true)
//        }.sortedBy { it.number }
//    }
//
//    Column(
//        modifier = Modifier
//            .verticalScroll(rememberScrollState())
//            .padding(10.dp)
//    ) {
//        Button(
//            shape = RoundedCornerShape(0.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.background
//            ),
//            elevation = null,
//            contentPadding = PaddingValues(0.dp),
//            modifier = Modifier
//                .padding(0.dp)
//                .fillMaxWidth()
//                .offset( x= 0.dp, y= (-16).dp),
//            onClick = {
//                AppExoPlayer.setPlaylist(
//                    songPlaylist = discList.flatten(),
//                    albumsList = albumsList,
//                )
//                AppExoPlayer.playMusic()
//                AppExoPlayer.setPlaylistToSelectedSong(discList.flatten()[0], discList.flatten())
//            }
//        ){
//            SpinningDisc(
//                albumsList = albumsList,
//                listOfUri = listUri,
//                song = firstSongFromList.value,
//                modifier = Modifier
//                    .offset( x= 0.dp, y= (-90).dp)
//                    .height(180.dp)
//            )
//        }
//        for (disc in filteredDisc){
//            DiscInfoHeader(
//                songsList = disc,
//                discIndex = filteredDisc.indexOf(disc)
//            )
//            HeaderOfDisc()
//            HorizontalDivider(
//                color = MaterialTheme.colorScheme.surface,
//                thickness = 2.dp
//            )
//            for (song in disc){
//                SongItem(
//                    song = song,
//                    songsList = discList.flatten(),
//                    albumsList = albumsList,
//                )
//            }
//        }
//    }
//}