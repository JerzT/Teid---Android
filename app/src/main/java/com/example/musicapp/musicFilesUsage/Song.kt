package com.example.musicapp.musicFilesUsage

import android.net.Uri

data class Song(
    val uri: Uri,
    val title: String?,
    val format: String?,
    val number: Int,
    val length: Int,
    var timePlayed: Int = 0,
)