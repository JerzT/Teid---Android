package com.example.musicapp.musicFilesUsage

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.settings.SettingsDataStore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.log

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun GetDirectory(
    database: DBHelper,
    uri: MutableState<Uri?>,
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
            AlbumsWhichExists.list.value.let {
                findAlbums(
                    uri = uri.value,
                    context = context,
                    albumsList = it,
                ).await()
            }

            for(album in AlbumsWhichExists.list.value){
                database.addAlbum(album)
            }
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