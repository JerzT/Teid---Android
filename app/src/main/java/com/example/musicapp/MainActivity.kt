package com.example.musicapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material.MaterialTheme
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicapp.logic.directory.changeNotValidDirectoryPathToUri
import com.example.musicapp.logic.mediaPlayer.AppExoPlayer
import com.example.musicapp.logic.mediaPlayer.PlaybackService
import com.example.musicapp.logic.settings.SettingsDataStore
import com.example.musicapp.ui.theme.MusicAppTheme
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

var sessionToken: SessionToken? = null
var controllerFuture: ListenableFuture<MediaController>? = null

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = "test"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel("1", name, importance)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)

        val settings = SettingsDataStore(this)
        var uri: Uri? = null

        GlobalScope.launch {
            settings.directoryPathFlow.collect { directoryPath ->
                uri = changeNotValidDirectoryPathToUri(directoryPath)
            }
        }

        setContent {
            MusicAppTheme {
                App(uri = uri)
            }
        }

        enableEdgeToEdge()

        sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(this, sessionToken!!).buildAsync()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        val notificationManager = baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.deleteNotificationChannel("1")

        super.onDestroy()
    }
}