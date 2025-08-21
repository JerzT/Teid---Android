package com.example.musicapp.newLogic.album

import kotlin.collections.iterator

fun connectDiscFromAlbums(
    albumList: List<Any>,
): MutableList<Any>{
    val newList = mutableListOf<Any>()

    val groupedAlbums = albumList.groupBy { album ->
        when(album){
            is Album -> album.name to album.artist
            else -> {}
        }
    }

    for (group in groupedAlbums) {
        val albumsInGroup = group.value

        if (albumsInGroup.size > 1) {
            newList.add(albumsInGroup)
        } else {
            newList.add(albumsInGroup.first())
        }
    }

    return newList
}