package com.example.musicapp.logic.mediaPlayer

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.legacy.MediaMetadataCompat
import androidx.media3.session.legacy.MediaSessionCompat
import com.example.musicapp.R
import com.example.musicapp.logic.database.setUpDatabase
import com.example.musicapp.logic.image.albumCoverCache
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(UnstableApi::class)
@SuppressLint("RestrictedApi", "Range")
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

    var coverUri: Uri? = null
    val db = setUpDatabase(context)
    val albumResult = db.getAlbumsFromUri(MediaPlayerApp.currentPlaying.value?.parentUri!!)
    albumResult.use { albumRow ->
        while (albumRow.moveToNext()){
            coverUri = if(albumRow.getString(albumRow.getColumnIndex("cover")) != "")
                albumRow.getString(albumRow.getColumnIndex("cover")).toUri() else null
        }
    }

    val mediaSession = MediaSessionCompat(context, "PlayerService")
    mediaSession.setMetadata(
        MediaMetadataCompat.Builder()
            .putString(MediaMetadata.METADATA_KEY_TITLE, MediaPlayerApp.currentPlaying.value?.title!!)

            .putString(MediaMetadata.METADATA_KEY_ARTIST, MediaPlayerApp.currentPlaying.value?.author!!)

            .putString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI, coverUri.toString())

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
