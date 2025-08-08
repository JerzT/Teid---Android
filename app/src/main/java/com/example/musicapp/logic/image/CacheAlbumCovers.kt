/*
package com.example.musicapp.logic.image

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
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
                    var albumWithImage: Album = albumList[0]

                    for (disc in albumList){
                        if (disc.cover != null){
                            albumWithImage = albumList[albumList.indexOf(disc)]
                        }
                    }

                    for(disc in albumList)
                    {
                        if (albumCoverCache[disc.uri.toString()] == null){
                            albumCoverCache[disc.uri.toString()] = loadAlbumCover(albumWithImage, context)
                        }
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
}*/
