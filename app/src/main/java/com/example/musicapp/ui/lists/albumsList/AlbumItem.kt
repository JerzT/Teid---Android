package com.example.musicapp.ui.lists.albumsList

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.musicapp.logic.album.Album
import com.example.musicapp.logic.image.albumCoverCache

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@Composable
fun AlbumItem(
    album: Any,
    navController: NavController,
) {
    var data: Album? = null
    var listUri: List<String>? = null
    when(album){
        is Album -> {
            data = album
            listUri = listOf(album.uri.toString())
        }
        is List<*> -> {
            data = (album as List<Album>)[0]
            listUri = album.sortedBy { it.cdNumber }
                .map { it.uri.toString() }.toMutableList()
        }
    }

    Button(
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.tertiary,
        ),
        contentPadding = PaddingValues(10.dp),
        onClick = {
            navController.navigate(
                Screen.SongList(
                listUri = listUri!!,
                name = data?.name.toString()
            ))
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
                    tint = MaterialTheme.colorScheme.surface,
                    contentDescription = "",
                )
            }
        }
    }
}