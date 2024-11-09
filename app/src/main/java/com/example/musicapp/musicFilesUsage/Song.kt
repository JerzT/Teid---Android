package com.example.musicapp.musicFilesUsage

import android.health.connect.datatypes.units.Length
import android.net.Uri

data class Song(
    val uri: Uri,
    val title: String?,
    val format: String?,
    val number: Int?,
    val length: Int?,
)