package com.example.musicapp.musicFilesUsage

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

object AlbumsWhichExists: ViewModel() {
    val list: MutableStateFlow<MutableList<Album>> = MutableStateFlow(mutableListOf())

    fun saveToList(getList: MutableList<Album>){
        list.value.clear()
        list.value.addAll(getList)
    }
}