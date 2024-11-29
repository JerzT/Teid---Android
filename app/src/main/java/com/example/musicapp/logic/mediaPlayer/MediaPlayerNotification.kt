package com.example.musicapp.logic.mediaPlayer

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.legacy.MediaMetadataCompat
import androidx.media3.session.legacy.MediaSessionCompat
import com.example.musicapp.R
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(UnstableApi::class)
@SuppressLint("RestrictedApi")
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

    val pauseAction: Notification.Action = Notification.Action.Builder(
        R.drawable.baseline_play_arrow_24, "Pause", playPausePendingIntent
    ).build()

    val mediaSession = MediaSessionCompat(context, "PlayerService")
    mediaSession.setMetadata(
        MediaMetadataCompat.Builder()

            // Title.
            .putString(MediaMetadata.METADATA_KEY_TITLE, MediaPlayerApp.currentPlaying.value?.title!!)

            // Artist.
            // Could also be the channel name or TV series.
            .putString(MediaMetadata.METADATA_KEY_ARTIST, MediaPlayerApp.currentPlaying.value?.author!!)

            // Album art.
            // Could also be a screenshot or hero image for video content
            // The URI scheme needs to be "content", "file", or "android.resource".
            .putString(
                MediaMetadata.METADATA_KEY_ALBUM_ART_URI, MediaPlayerApp.currentPlaying.value?.parentUri!!.toString()
            )


        // Duration.
        // If duration isn't set, such as for live broadcasts, then the progress
        // indicator won't be shown on the seekbar.
        .putLong(MediaMetadata.METADATA_KEY_DURATION, MediaPlayerApp.mediaPlayer!!.duration.toLong()) // 4

        .build()
    )

    val mediaStyle = Notification.MediaStyle().setMediaSession(mediaSession.sessionToken.token as MediaSession.Token?)


    val notification = Notification.Builder(context, "1")
        .setVisibility(Notification.VISIBILITY_PUBLIC)
        .setSmallIcon(R.drawable.baseline_stop_24)
        .setContentTitle("Teid")
        .setOngoing(true)
        //add media controls
        .setStyle(mediaStyle)
        .addAction(pauseAction)
        .build()

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    notificationManager.notify(
        Random.nextInt(),
        notification
    )
}
