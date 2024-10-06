package com.example.musicapp.musicFilesUsage

import android.net.Uri

data class Album(
    val name: String?,
    val uri: Uri,
    val cover: Uri?,
    val artist: String?,
    val year: String?
)