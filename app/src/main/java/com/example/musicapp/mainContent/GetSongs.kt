package com.example.musicapp.mainContent

import android.content.Context
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.example.musicapp.musicFilesUsage.Album

fun GetSongs(
    album: Album,
    context: Context
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
        var files = documentFile.listFiles()
        for (file in files){
            if(file.type?.let {
                type -> supportedAudioFormats.any{
                    type.contains(it, ignoreCase = true) }} == true
                )
            {
                Log.v("test1", file.name.toString())
            }
        }
    }
    else{
        Log.v("test1", "no directory")
    }
}