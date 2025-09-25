package com.example.musicapp.fragments.library

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.logic.artist.Artist

object LibraryLiveViewModel: ViewModel() {
    private val _artistSet = MutableLiveData<MutableSet<Artist>>(mutableSetOf())
    val artistSet: MutableLiveData<MutableSet<Artist>> get() = _artistSet
    private val _seenArtistSet: MutableSet<String?> = mutableSetOf()

    fun addArtist(artist: Artist){
        val updated = _artistSet.value

        if(artist.name !in _seenArtistSet){
            updated!!.add(artist)
            _seenArtistSet.add(artist.name)
            _artistSet.postValue(updated)
        }
    }


}