package com.example.musicapp.musicFilesUsage

import android.content.Context
import android.media.MediaMetadataRetriever
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

@OptIn(DelicateCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.P)
fun getSongs(
    album: Album,
    context: Context,
    songsList: MutableList<Song>
): Deferred<Unit> = GlobalScope.async {
    val documentFile = DocumentFile.fromTreeUri(context, album.uri)

    val supportedAudioFormats = listOf(
        "mp3", "flac", "m4a",
        "aac", "wav", "ogg",
        "amr", "mid", "xmf",
        "mxmf", "rtttl", "rtx",
        "ota", "imy", "3gp",
        "ts", "mkv", "mpeg")

    if(documentFile != null && documentFile.isDirectory){
        val files = documentFile.listFiles()
        for (file in files){
            if(file.type?.let {
                type -> supportedAudioFormats.any{
                    type.contains(it, ignoreCase = true) }} == true
                )
            {
                val metadata = getMetadata(
                    file = file,
                    context = context)
                val song = Song(
                    uri = file.uri,
                    title = metadata["songName"],
                    format = file.type,
                )

                songsList.add(song)
            }
        }
    }
    else{
        Log.v("test1", "no directory")
    }
}

@RequiresApi(Build.VERSION_CODES.P)
private fun getMetadata(file: DocumentFile, context: Context): Map<String?, String?> {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(context, file.uri)

        val songName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        val artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            ?: retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)
            ?: retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR)
            ?: retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER)
        val songYear = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)
        val cdNumber = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER)
            ?: "1"

        mapOf(
            "albumName" to songName,
            "artistName" to artistName,
            "albumYear" to songYear,
            "cdNumber" to cdNumber[0].toString(),
        )
    } catch (e: Exception) {
        mapOf()
    } finally {
        retriever.release()
    }
}
