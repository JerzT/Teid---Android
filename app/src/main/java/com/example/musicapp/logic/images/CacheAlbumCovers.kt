package com.example.musicapp.logic.images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import com.example.musicapp.logic.album.Album
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

val albumsCovers = mutableMapOf<Uri, Bitmap>()

fun cacheAlbumsCovers(
    albumsList: MutableList<Any>,
    context: Context
){
    for(album in albumsList){
        GlobalScope.launch {
            when(album){
                is Album ->{
                    if(album.cover != null){
                        val source = ImageDecoder.createSource(context.contentResolver, album.cover)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        albumsCovers.put(key = album.uri, value = bitmap)
                    }
                    else{
                        val bitmap = getEmbeddedImage(album, context)
                        if( bitmap != null ){
                            albumsCovers.put(album.uri, bitmap)
                        }
                    }
                }
                is List<*> ->{
                    val albumsList = album as List<Album>
                    var albumWithImage = albumsList[0]
                    for(alb in albumsList){
                        if(alb.cover != null){
                            albumWithImage = alb
                            val source = ImageDecoder.createSource(context.contentResolver, alb.cover)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            albumsCovers.put(key = alb.uri, value = bitmap)
                            return@launch
                        }
                        else{
                            val bitmap = getEmbeddedImage(alb, context)
                            if( bitmap != null ){
                                albumsCovers.put(alb.uri, bitmap)
                                return@launch
                            }
                        }
                    }
                }
            }
        }
    }

}