package com.example.musicapp.logic.image

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.musicapp.logic.album.Album
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val albumCoverCache = mutableStateMapOf<String, ImageBitmap?>()

@OptIn(DelicateCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
suspend fun cacheAlbumCovers(albums: MutableList<Any>, context: Context) {
    albums.forEach { album ->
        when(album){
            is Album->{
                GlobalScope.launch {
                    if (albumCoverCache[album.uri.toString()] == null) {
                        albumCoverCache[album.uri.toString()] = loadAlbumCover(album, context)
                    }
                }
            }
            is List<*> -> {
                GlobalScope.launch {
                    val albumList = album as List<Album>
                    var albumFromList: Album = albumList[0]

                    for (albumA in albumList){
                        if (albumA.cover != null){
                            albumFromList = albumList[albumList.indexOf(albumA)]
                        }
                    }

                    if (albumCoverCache[albumList[0].uri.toString()] == null){
                        albumCoverCache[albumList[0].uri.toString()] = loadAlbumCover(albumFromList, context)
                    }
                }
            }
        }
    }
}

private suspend fun loadAlbumCover(album: Album, context: Context): ImageBitmap? {
    return withContext(Dispatchers.IO) {
        if(album.cover != null){
            album.cover.let { coverUri ->
                context.contentResolver.openInputStream(coverUri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                }
            }
        }
        else{
            getEmbeddedImage(album, context)
        }
    }
}