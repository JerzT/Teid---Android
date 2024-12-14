package com.example.musicapp.logic.song

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import com.example.musicapp.logic.database.setUpDatabase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

@OptIn(DelicateCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.P)
fun getSongs(
    uri: Uri,
    context: Context,
    songsList: MutableList<Song>
): Deferred<Unit> = GlobalScope.async {
    val database = setUpDatabase(context)
    val documentFile = DocumentFile.fromTreeUri(context, uri)

    val supportedAudioFormats = listOf(
        "mp3", "flac", "m4a",
        "aac", "wav", "ogg",
        "amr", "mid", "xmf",
        "mxmf", "rtttl", "rtx",
        "ota", "imy", "3gp",
        "ts", "mkv", "wv")

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

                val formatedTitle = file.name?.removeFileExtension()

                val song = Song(
                    uri = file.uri,
                    parentUri = documentFile.uri,
                    title = metadata["songName"] ?: formatedTitle,
                    author = metadata["artistName"] ?: "",
                    format = file.type,
                    number = metadata["songNumber"]?.toInt() ?: 0,
                    length = metadata["songLength"]?.toInt() ?: 0,
                )
                database.addSong(song = song)
                songsList.add(song)
            }
        }
    }
    else{
        Log.v("test1", "no directory")
    }
}

@RequiresApi(Build.VERSION_CODES.P)
private fun getMetadata(file: DocumentFile, context: Context): Map<String, String?> {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(context, file.uri)

        val songName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)?.trim()
        val artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)?.trim()
            ?: retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)?.trim()
            ?: retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR)?.trim()
            ?: retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER)?.trim()
        val songYear = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)?.trim()
        val cdNumber = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER)?.trim()
            ?: "1"
        var songNumber = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)?.trim()
            ?: "1"
        val songLength = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.trim()

        if(songNumber.contains("/")){
            songNumber = songNumber.split("/")[0]
        }

        mapOf(
            "songName" to songName,
            "artistName" to artistName,
            "songYear" to songYear,
            "cdNumber" to cdNumber[0].toString(),
            "songNumber" to songNumber,
            "songLength" to songLength,
        )
    } catch (e: Exception) {
        mapOf()
    } finally {
        retriever.release()
    }
}

private fun String.removeFileExtension(): String {
    val lastDotIndex = this.lastIndexOf(".")
    return if (lastDotIndex != -1) {
        this.substring(0, lastDotIndex)
    } else {
        this
    }
}
