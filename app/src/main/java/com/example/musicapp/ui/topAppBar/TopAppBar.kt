package com.example.musicapp.ui.topAppBar

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.musicapp.R
import com.example.musicapp.SETTINGS
import com.example.musicapp.SONG_LIST
import com.example.musicapp.Screen

@SuppressLint("RestrictedApi")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarCustom(
    title: String,
    navController: NavController? = null,
    currentScreen: String,
){
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.surface,
            actionIconContentColor = MaterialTheme.colorScheme.surface,
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentScreen == SONG_LIST || currentScreen == SETTINGS){
                    IconButton(
                        onClick = {
                            navController?.popBackStack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                }
                Text(
                    text = title,
                    fontSize = 24.sp,
                    maxLines = 1
                )
            }
        },
        actions = {
            Row {
                //settings
                if (currentScreen != SETTINGS){
                    Button(
                        onClick = { navController?.navigate(Screen.Settings) },
                        contentPadding = PaddingValues(0.dp),
                        elevation = null,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0x00FFFFFF),
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_settings_24),
                            contentDescription = "settings",
                            modifier = Modifier
                                .fillMaxSize(0.75f),
                        )
                    }
                }
            }
        }
    )
}