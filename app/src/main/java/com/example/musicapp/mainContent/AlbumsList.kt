package com.example.musicapp.mainContent

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.musicapp.musicFilesUsage.AlbumsWhichExists

@Composable
fun AlbumsList(){
    Log.v("test1", AlbumsWhichExists.list.toString())
    for (album in AlbumsWhichExists.list){
        Button(onClick = {

        }) {
            Text(text = "test")
        }
    }
}