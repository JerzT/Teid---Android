package com.example.musicapp.musicFilesUsage

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.documentfile.provider.DocumentFile

suspend fun getEmbeddedImage(
    album: Album,
    context: Context
): ImageBitmap?{
    var embeddedPicture: ImageBitmap? = null

    val documentFile = DocumentFile.fromTreeUri(context, album.uri)

    if(documentFile != null && documentFile.isDirectory){
        val supportedAudioFormats = listOf(
            "mp3", "flac", "m4a",
            "aac", "wav", "ogg",
            "amr", "mid", "xmf",
            "mxmf", "rtttl", "rtx",
            "ota", "imy", "3gp",
            "ts", "mkv", "mpeg")

        val files = documentFile.listFiles()

        for(file in files){
            if(file.type?.let {
                        type -> supportedAudioFormats.any {
                    type.contains(it, ignoreCase = true) } } == true)
            {
                val retriever = MediaMetadataRetriever()
                try {
                    retriever.setDataSource(context, file.uri)

                    embeddedPicture = (BitmapFactory.decodeByteArray(
                        retriever.embeddedPicture, 0, retriever.embeddedPicture!!.size))
                        .asImageBitmap()
                }
                catch(e: Exception){
                    {}
                }
            }
        }
    }

    return embeddedPicture
}