package com.example.musicapp.settings

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Base64

val Context.settings: DataStore<Preferences> by preferencesDataStore(name = "settings")


class SettingsDataStore(private val context: Context) {
    companion object {
        private val DIRECTORY_KEY = stringPreferencesKey("directory_storage")
    }

    suspend fun saveDirectoryPath(directoryPath: String) {
        context.settings.edit { preferences ->
            preferences[DIRECTORY_KEY] = directoryPath
        }
    }

    val directoryPathFlow: Flow<String?> = context.settings.data
        .map { preferences ->
            preferences[DIRECTORY_KEY]
        }
}