package com.example.musicapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.example.musicapp.bottomBar.BottomBarCustom
import com.example.musicapp.musicFilesUsage.GetDirectory
import com.example.musicapp.searchBar.SearchBar
import com.example.musicapp.searchBar.SearchShadow
import com.example.musicapp.topAppBar.TopAppBarCustom
import com.example.musicapp.ui.theme.MusicAppTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(
    showBackground = true,
    widthDp = 300,
    heightDp = 650,
)
@Composable
fun App(){
    val isPlaying: Boolean = true

    MusicAppTheme {
        Scaffold(
            topBar = { TopAppBarCustom() },
            bottomBar = { if(isPlaying) BottomBarCustom() },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                SearchBar(
                    modifier = Modifier
                        .zIndex(1f)
                )
                GetDirectory()
            }
        }
    }
}