package com.example.musicapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.musicapp.logic.mediaPlayer.MediaPlayerApp;

public class PlaybackService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        MediaPlayerApp.INSTANCE.releasePlayer();
    }
}
