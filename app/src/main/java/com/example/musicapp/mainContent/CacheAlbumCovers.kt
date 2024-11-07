package com.example.musicapp.mainContent

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.musicFilesUsage.getEmbeddedImage
import com.example.musicapp.settings.SettingsDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val albumCoverCache = mutableStateMapOf<String, ImageBitmap?>()

@RequiresApi(Build.VERSION_CODES.O)
suspend fun cacheAlbumCovers(albums: List<Album>, context: Context) {
    albums.forEach { album ->
        if (albumCoverCache[album.uri.toString()] == null) {
            albumCoverCache[album.uri.toString()] = loadAlbumCover(album, context)
        }
    }
}

private suspend fun loadAlbumCover(album: Album, context: Context): ImageBitmap? {
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