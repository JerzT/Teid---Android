package com.example.musicapp.logic.mediaPlayer

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.OptIn
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaStyleNotificationHelper
import com.example.musicapp.R
import kotlin.random.Random

@OptIn(UnstableApi::class)
fun mediaPlayerNotification(
    context: Context,
    //imageBitmap: ImageBitmap
){
    val notificationManager = context.getSystemService(NotificationManager::class.java)

    val notification = NotificationCompat.Builder(context, "1")
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setSmallIcon(R.drawable.baseline_stop_24)
        .setContentTitle("test")
        //add media controls
        //.addAction(R.drawable.baseline_skip_previous_24, "Previous", prevPendingIntent)
        .build()

    notificationManager.notify(
        Random.nextInt(),
        notification
    )
}