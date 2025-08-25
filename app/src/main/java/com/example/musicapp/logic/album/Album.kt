package com.example.musicapp.logic.album

import android.net.Uri

data class Album(
    val name: String?,
    val uri: Uri,
    val cover: Uri?,
    val artist: String?,
    val year: String?,
    val cdNumber: Int?,
)