package com.example.musicapp.mainContent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R
import com.example.musicapp.musicFilesUsage.Album

@Composable
fun AlbumsList(albumsList: List<Album>) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(albumsList) { album ->
            AlbumItem(album = album)
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
    }
}

@Composable
fun AlbumItem(album: Album) {
    Button(
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.surface,
        ),
        contentPadding = PaddingValues(10.dp),
        onClick = {}
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
         /*if (album.cover != null) {
                AsyncImage(
                    model = album.cover,
                    contentDescription = album.name,
                    modifier = Modifier
                        .height(36.dp)
                        .padding(end = 10.dp),
                    contentScale = ContentScale.FillHeight,
                    placeholder = painterResource(R.drawable.baseline_replay_24),
                )
            }*/

            Text(
                text = album.name.toString(),
                color = MaterialTheme.colorScheme.surface,
                fontSize = 20.sp,
            )
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_right_24),
                    contentDescription = "",
                )
            }
        }
    }
}