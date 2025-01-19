package com.example.musicapp.logic.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.documentfile.provider.DocumentFile
import com.example.musicapp.logic.album.Album
import java.io.ByteArrayOutputStream

fun getEmbeddedImage(
    album: Album,
    context: Context
): ImageBitmap? {
    val documentFile = DocumentFile.fromTreeUri(context, album.uri)

    if (documentFile == null || !documentFile.isDirectory) return null

    val supportedAudioFormats = listOf(
        "mp3", "flac", "m4a", "aac", "wav", "ogg",
        "amr", "mid", "xmf", "mxmf", "rtttl", "rtx",
        "ota", "imy", "3gp", "ts", "mkv", "mpeg"
    )

    val files = documentFile.listFiles()

    val retriever = MediaMetadataRetriever()
    try {
        for (file in files) {
            if (file.type?.let { type ->
                    supportedAudioFormats.any { type.contains(it, ignoreCase = true) }
                } == true
            ) {
                try {
                    retriever.setDataSource(context, file.uri)

                    val pictureData = retriever.embeddedPicture
                    if (pictureData != null) {
                        val bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.size)

                        val byteArrayOutputStream = ByteArrayOutputStream()

                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream)

                        val compressedBitmap = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size())

                        return compressedBitmap?.asImageBitmap()
                    }
                } catch (e: Exception) {
                    //
                }
            }
        }
    } finally {
        retriever.release()
    }

    return null
}