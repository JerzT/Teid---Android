package com.example.musicapp.ui.lists.albumsList

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.musicapp.logic.album.Album

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
                when (album) {
                    is Album -> album.name?.contains(searchText.value, ignoreCase = true) == true
                    is List<*> -> (album as List<Album>)[0].name?.contains(searchText.value, ignoreCase = true) == true
                    else -> false
                }
            }
        }
    }

    // Group albums by the first letter of their name or another category
    val groupedAlbums = filteredAlbums
        .groupBy { album ->
            when (album) {
                is Album ->{
                    val firstChar = album.name?.firstOrNull()
                    if (firstChar?.isDigit() == true && firstChar in '1'..'9'){
                        "1 - 9"
                    }else{
                        firstChar?.uppercaseChar() ?: "Other"
                    }
                }
                is List<*> ->{
                    val firstChar = (album as List<Album>)[0].name?.firstOrNull()
                    if (firstChar?.isDigit() == true && firstChar in '1'..'9'){
                        "1 - 9"
                    }else {
                        firstChar?.uppercaseChar() ?: "Other"
                    }
                }
                else -> "Other"
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
        // Iterate through each category
        groupedAlbums.forEach { (category, albumsInCategory) ->
            item {
                // Category header
                CategoryHeader(text = category.toString())
            }

            // Display the albums in this category
            items(
                albumsInCategory,
                key = { album ->
                    when (album) {
                        is Album -> album.uri
                        is List<*> -> (album as List<Album>)[0].uri
                        else -> ""
                    }
                },
                itemContent = { album ->
                    AlbumItem(
                        album = album,
                        navController = navController,
                    )
                }
            )
        }
    }
}

@Composable
fun CategoryHeader(
    text: String,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.surface,
        fontSize = 24.sp,
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp)
    )
}