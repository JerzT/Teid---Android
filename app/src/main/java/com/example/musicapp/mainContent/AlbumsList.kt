package com.example.musicapp.mainContent

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.documentfile.provider.DocumentFile
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.musicapp.R
import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.musicFilesUsage.getCover
import com.example.musicapp.musicFilesUsage.getMetadata
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val albumCoverCache = mutableStateMapOf<String, ImageBitmap?>()

@Composable
fun AlbumsList(albumsList: SnapshotStateList<Album>) {
    val context = LocalContext.current

    LaunchedEffect(albumsList) {
        cacheAlbumCovers(albumsList, context)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        for(album in albumsList){
            AlbumItem(album = album)
        }
    }
}

suspend fun cacheAlbumCovers(albums: List<Album>, context: Context) {
    albums.forEach { album ->
        if (albumCoverCache[album.name] == null) {
            albumCoverCache[album.name.toString()] = loadAlbumCover(album, context)
        }
    }
}

suspend fun loadAlbumCover(album: Album, context: Context): ImageBitmap? {
    if(album.cover != null){
        return withContext(Dispatchers.IO){
            val coverUri: Uri = album.cover
            context.contentResolver.openInputStream(coverUri).use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                return@withContext bitmap?.asImageBitmap()
            }
        }
    }
    else{
        return withContext(Dispatchers.IO) {
            getEmbeddedImage(album, context)
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
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
            modifier = Modifier.fillMaxWidth(),
        ) {
            val cachedCover = albumCoverCache[album.name]

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