package com.example.musicapp.logic.song

import android.content.Context
import android.util.Log
import com.example.musicapp.logic.database.setUpDatabase

fun synchronizeSongs(
    songsFromDatabase: MutableList<Any>,
    songsInDirectory: MutableList<Any>,
    context: Context,
){
    val database = setUpDatabase(context)

    val songsFromDatabaseSet = songsFromDatabase.toSet()
    val songsInDirectorySet = songsInDirectory.toSet()

    val songsToDelete = songsFromDatabaseSet - songsInDirectorySet
    val songsToAdd = songsInDirectorySet - songsFromDatabaseSet

    for (song in songsToDelete){
        when(song){
            is Song ->{
                database.deleteSong(song)
            }
        }
    }
    for(song in songsToAdd){
        when(song){
            is Song ->{
                database.addSong(song)
            }
        }
    }
}