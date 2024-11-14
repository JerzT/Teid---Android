package com.example.musicapp.mainContent

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.musicapp.R
import com.example.musicapp.Screen
import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.musicFilesUsage.Song
import com.example.musicapp.musicFilesUsage.getSongs
import kotlinx.coroutines.launch
import java.net.URLEncoder
import kotlin.math.round

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AlbumsList(
    albumsList: List<Album>,
    navController: NavController,
    searchText: MutableState<String>,
) {
    val state = remember { LazyListState() }

    val filteredAlbums = albumsList.filter { album ->
        album.name!!.contains(searchText.value, ignoreCase = true)
    }

    LazyColumn(
        state = state,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 0.dp)
            .zIndex(1f)
    ) {
        items(1){
            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )
        }

        items(
            filteredAlbums,
            key = { album -> album.uri },
            itemContent = { album ->
                AlbumItem(
                    album = album,
                    navController = navController,
                )
            }
        )

        items(1){
            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@Composable
private fun AlbumItem(
    album: Album,
    navController: NavController,
) {
    Button(
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.surface,
        ),
        contentPadding = PaddingValues(10.dp),
        onClick = {
            val stupid = URLEncoder.encode(album.uri.toString(), "UTF-8")
            navController.navigate(route = Screen.SongList.withArgs(stupid, album.name.toString()))
        },
        modifier = Modifier
            .zIndex(1f)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
          val cachedCover = albumCoverCache[album.uri.toString()]

            if (cachedCover != null) {
                Image(
                    painter = BitmapPainter(cachedCover),
                    contentDescription = album.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .graphicsLayer {
                            shape = RoundedCornerShape(3.dp)
                            clip = true
                        }
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(3.dp)
                        )
                        .size(42.dp)
                )
            }

            Text(
                text = album.name.toString(),
                color = MaterialTheme.colorScheme.surface,
                fontSize = 20.sp,
                maxLines = 1,
                modifier = Modifier
                    .weight(15f)
                    .padding(10.dp, 0.dp)
            )
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_right_24),
                    contentDescription = "",
                )
            }
        }
    }
}