package com.example.musicapp.logic.directory

import android.content.Context
import com.example.musicapp.logic.settings.SettingsDataStore
import com.example.musicapp.newLogic.DirectoryUri
import com.example.musicapp.newLogic.albumsList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
fun getAlbumsFromDirectory(settingsDataStore: SettingsDataStore, context: Context): MutableList<Any> {
    var albumsInDirectory: MutableList<Any> = mutableListOf()
    GlobalScope.launch {
        settingsDataStore.saveDirectoryPath(DirectoryUri.uri.toString())
        findAlbums(
            uri = DirectoryUri.uri,
            context = context,
            albumsList = albumsList
        ).await()
    }
    albumsInDirectory = albumsList
    return albumsInDirectory

}