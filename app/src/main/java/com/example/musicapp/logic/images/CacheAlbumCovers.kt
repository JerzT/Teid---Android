package com.example.musicapp.logic.images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import com.example.musicapp.logic.album.Album
import kotlinx.coroutines.GlobalScope
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
                        albumsCovers.put(album.uri, bitmap)
                    }
                    else{
                        val bitmap = getEmbeddedImage(album.uri, context)
                        if( bitmap != null ){
                            albumsCovers.put(album.uri, bitmap)
                        }
                    }
                }
                is List<*> ->{
                    val albumsList = album as List<Album>
                    for(alb in albumsList){
                        if(alb.cover != null){
                            val source = ImageDecoder.createSource(context.contentResolver, alb.cover)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            albumsCovers.put(albumsList[0].uri, bitmap)
                            return@launch
                        }
                        else{
                            val bitmap = getEmbeddedImage(alb.uri, context)
                            if( bitmap != null ){
                                albumsCovers.put(albumsList[0].uri, bitmap)
                                return@launch
                            }

                        }
                    }
                }
            }
        }
    }
}

fun cacheAlbumCover(
    albumUri: Uri,
    albumCover: Uri?,
    context: Context?
){
    GlobalScope.launch {
        if(albumCover != null){
            val source = ImageDecoder.createSource(context!!.contentResolver, albumCover)
            val bitmap = ImageDecoder.decodeBitmap(source)
            albumsCovers.put(albumUri, bitmap)
        }
        else{
            val bitmap = getEmbeddedImage(albumUri, context)
            if( bitmap != null ){
                albumsCovers.put(albumUri, bitmap)
            }
        }
    }
}