package com.example.musicapp.logic.mediaPlayer

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.musicapp.R
import kotlin.random.Random

fun mediaPlayerNotification(
    context: Context,
    //imageBitmap: ImageBitmap
){

    val playPauseIntent = Intent(context, PlaybackService::class.java).apply {
        action = ACTION_PLAY
    }
    val playPausePendingIntent = PendingIntent.getService(
        context, 0, playPauseIntent, PendingIntent.FLAG_MUTABLE
    )

    val notification = NotificationCompat.Builder(context, "1")
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setSmallIcon(R.drawable.baseline_stop_24)
        .setContentTitle("testtest")
        .setOngoing(true)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        //add media controls
        .addAction(R.drawable.baseline_play_arrow_24, "Play", playPausePendingIntent)
        .build()

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(
        Random.nextInt(),
        notification
    )
}