package com.example.musicapp.musicFilesUsage

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class Album(
    val name: String?,
    val uri: Uri,
    val cover: Uri?,
    val artist: String?,
    val year: String?,
    val cdNumber: Int?,
)