package com.example.musicapp.logic.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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