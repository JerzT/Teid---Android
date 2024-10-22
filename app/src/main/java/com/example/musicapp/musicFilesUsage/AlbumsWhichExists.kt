package com.example.musicapp.musicFilesUsage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

@Composable
fun albumsWhichExists(): SnapshotStateList<Album> {
    val list = remember { mutableStateListOf<Album>()}

    return list
}