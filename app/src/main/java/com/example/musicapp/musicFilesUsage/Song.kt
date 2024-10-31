package com.example.musicapp.musicFilesUsage

import android.net.Uri

data class Song(
    val uri: Uri,
    val title: String?,
    val format: String?,
)