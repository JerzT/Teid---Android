package com.example.musicapp.musicFilesUsage

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.musicapp.mainContent.cacheAlbumCovers
import com.example.musicapp.settings.SettingsDataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun GetDirectory(
    uri: MutableState<Uri?>,
    albumsList: MutableList<Album>,
) {
    val context = LocalContext.current

    val settings = SettingsDataStore(context)

    // this part of code cast Director selector
    val directoryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uriGot: Uri? ->
        uri.value = uriGot

        uriGot?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }
        GlobalScope.launch {
            settings.saveDirectoryPath(uri.toString())

            findAlbums(
                uri = uri.value,
                context = context,
                albumsList = albumsList,
            ).await()

            albumsList.sortBy { it.name }

            cacheAlbumCovers(albumsList, context)
        }
    }

    Button(
        onClick = { directoryPickerLauncher.launch(null) },
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.surface,
            containerColor = MaterialTheme.colorScheme.tertiary,
        ),
    ) {
        Text(
            text = "Pick Directory",
            fontSize = 20.sp
        )
    }
}