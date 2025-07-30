package com.example.musicapp.logic.directory
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import com.example.musicapp.logic.album.findAlbums
import com.example.musicapp.logic.settings.SettingsDataStore
import com.example.musicapp.logic.settings.settings
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.P)
suspend fun GetDirectory(
    uri: Uri?,
    albumsList: MutableList<Any>,
    navController: NavController,
) {
    val settings = SettingsDataStore()

    // this part of code cast Director selector
    val directoryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uriGot: Uri? ->
        uri = uriGot

        uriGot?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }
        GlobalScope.launch {
            settings.saveDirectoryPath(uri.toString())

            findAlbums(
                uri = uri,
                context = context,
                albumsList = albumsList,
            ).await()
        }
    }
}

private class GetDirectory: ComponentActivity() {
    val dirRequest = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        uri?.let {
            // call this to persist permission across decice reboots
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            // do your stuff
        }
        GlobalScope.launch {
            settings.saveDirectoryPath(uri.toString())

            findAlbums(
                uri = uri,
                context = context,
                albumsList = albumsList,
            ).await()
        }
    }
}