package com.example.musicapp.ui.lists

import android.annotation.SuppressLint
import android.os.Build
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.musicapp.R
import com.example.musicapp.Screen
import com.example.musicapp.logic.image.albumCoverCache
import com.example.musicapp.logic.album.Album
import java.net.URLEncoder

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AlbumsList(
    albumsList: List<Any>,
    navController: NavController,
    searchText: MutableState<String>,
    state: LazyListState,
) {
    val filteredAlbums by remember(searchText.value) {
        derivedStateOf {
            albumsList.filter { album ->
                when(album){
                    is Album -> album.name?.contains(searchText.value, ignoreCase = true) == true
                    is List<*> -> (album as List<Album>)[0].name?.contains(searchText.value, ignoreCase = true) == true
                    else -> false
                }
            }
        }
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
            key = { album ->
                when(album){
                    is Album -> {
                        album.uri
                    }
                    is List<*> -> {
                        (album as List<Album>)[0].uri
                    }
                    else -> {}
                }},
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
    album: Any,
    navController: NavController,
) {
    var data: Album? = null
    when(album){
        is Album -> {
            data = album
        }
        is List<*> -> {
            data = (album as List<Album>)[0]
        }
    }

    Button(
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.surface,
        ),
        contentPadding = PaddingValues(10.dp),
        onClick = {
            val stupid = URLEncoder.encode(data?.uri.toString(), "UTF-8")
            navController.navigate(route = Screen.SongList.withArgs(stupid, data?.name.toString()))
        },
        modifier = Modifier
            .zIndex(1f)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
          val cachedCover = albumCoverCache[data?.uri.toString()]

            if (cachedCover != null) {
                Image(
                    painter = BitmapPainter(cachedCover),
                    contentDescription = data?.name,
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
                text = data?.name.toString(),
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