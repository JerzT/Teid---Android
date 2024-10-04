package com.example.musicapp.musicFilesUsage

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

fun findFiles(
    uri: Uri?,
    context: Context,
    list: MutableList<String>,
): Deferred<Unit> = GlobalScope.async{
    if(uri == null){
        return@async
    }
    val documentFile = DocumentFile.fromTreeUri(context, uri)
    if (documentFile != null && documentFile.isDirectory) {
        val files = documentFile.listFiles()
        for (file in files) {
            if (file.isDirectory){
                findFiles(file.uri, context, list)
            }
            else{
                list += documentFile.name.toString()
            }
        }
    }
}