package com.example.musicapp.fragments.library

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.logic.artist.Artist

object LibraryLiveViewModel: ViewModel() {
    private val _artistSet = MutableLiveData<MutableSet<Artist>>(mutableSetOf())
    val artistSet: MutableLiveData<MutableSet<Artist>> get() = _artistSet

    fun addArtist(artist: Artist){
        val updated = _artistSet.value
        updated!!.add(artist)
        Log.d("test1", "$updated")
        _artistSet.postValue(updated)
    }
}