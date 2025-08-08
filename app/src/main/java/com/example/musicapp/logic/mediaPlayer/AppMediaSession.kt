package com.example.musicapp.logic.mediaPlayer

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.example.musicapp.R
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

private const val SHUFFLE = "Shuffle"
private const val NO_SHUFFLE = "NoShuffle"
private const val NO_LOOP = "NoLoop"
private const val LOOP_SONG = "LoopSong"
private const val LOOP_ALBUM = "LoopAlbum"

val noShuffleButton = CommandButton.Builder()
    .setDisplayName("NoShuffle")
    .setIconResId(R.drawable.baseline_no_shuffle_24)
    .setSessionCommand(SessionCommand(NO_SHUFFLE, Bundle()))
    .build()

val shuffleButton = CommandButton.Builder()
    .setDisplayName("Shuffle")
    .setIconResId(R.drawable.baseline_shuffle_24)
    .setSessionCommand(SessionCommand(SHUFFLE, Bundle()))
    .build()

val noLoopButton = CommandButton.Builder()
    .setDisplayName("NoLoop")
    .setIconResId(R.drawable.baseline_no_replay_24)
    .setSessionCommand(SessionCommand(NO_LOOP, Bundle()))
    .build()

val loopAlbumButton = CommandButton.Builder()
    .setDisplayName("LoopAlbum")
    .setIconResId(R.drawable.baseline_loop_24)
    .setSessionCommand(SessionCommand(LOOP_ALBUM, Bundle()))
    .build()

val loopSongButton = CommandButton.Builder()
    .setDisplayName("LoopSong")
    .setIconResId(R.drawable.baseline_replay_24)
    .setSessionCommand(SessionCommand(LOOP_SONG, Bundle()))
    .build()

object AppMediaSession{
    var mediaSession: MediaSession? = null

    @OptIn(UnstableApi::class)
    fun setUpMediaSession(context: Context, player: ExoPlayer){
        mediaSession = MediaSession.Builder(context, player)
            .setCallback(CustomMediaSessionCallback())
            .setCustomLayout(ImmutableList.of(noShuffleButton, noLoopButton))
            .build()
    }
}

private class CustomMediaSessionCallback: MediaSession.Callback {
    @OptIn(UnstableApi::class)
    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
        val sessionCommands = MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
            .add(SessionCommand(NO_SHUFFLE, Bundle.EMPTY))
            .add(SessionCommand(SHUFFLE, Bundle.EMPTY))
            .add(SessionCommand(NO_LOOP, Bundle.EMPTY))
            .add(SessionCommand(LOOP_ALBUM, Bundle.EMPTY))
            .add(SessionCommand(LOOP_SONG, Bundle.EMPTY))
            .build()

        return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
            .setAvailableSessionCommands(sessionCommands)
            .build()
    }
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
        if (customCommand.customAction == SHUFFLE || customCommand.customAction == NO_SHUFFLE) {
            //AppExoPlayer.shufflePlaylist()
        }
        if (customCommand.customAction == NO_LOOP ||
            customCommand.customAction == LOOP_SONG ||
            customCommand.customAction == LOOP_ALBUM) {
            //handleLooping()
        }
        return Futures.immediateFuture(
            SessionResult(SessionResult.RESULT_SUCCESS)
        )
    }
}