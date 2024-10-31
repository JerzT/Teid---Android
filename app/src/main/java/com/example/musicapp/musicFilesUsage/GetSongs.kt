package com.example.musicapp.musicFilesUsage

import android.content.Context
import android.util.Log
import androidx.documentfile.provider.DocumentFile

fun getSongs(
    album: Album,
    context: Context,
    songsList: MutableList<Song>
) {
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
                val song = Song(
                    uri = file.uri,
                    title = file.name,
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