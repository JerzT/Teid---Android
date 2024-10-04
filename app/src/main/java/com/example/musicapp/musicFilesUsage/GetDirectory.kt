package com.example.musicapp.musicFilesUsage

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.documentfile.provider.DocumentFile

@Composable
fun GetDirectory() {
    val context = LocalContext.current
    val selectedUri = remember { mutableStateOf<Uri?>(null) }

    // this part of code cast Director selector
    val directoryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        selectedUri.value = uri

        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }
        val list: MutableList<String> = mutableListOf()
        findFiles(
            uri = uri,
            context = context,
            list = list,
        )
        Log.v("test1", list.toString())
    }

    Button(onClick = { directoryPickerLauncher.launch(null) }) {
        Text(text = "Pick Directory")
    }


    selectedUri.value?.let {
        Text(text = "Selected directory: $it")
    } ?: Text(text = "No directory selected")
}