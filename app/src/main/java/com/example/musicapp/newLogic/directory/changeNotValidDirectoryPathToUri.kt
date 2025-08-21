package com.example.musicapp.newLogic.directory

import android.net.Uri
import androidx.core.net.toUri

fun changeNotValidDirectoryPathToUri(
    directoryPath: String?
): Uri?{
    if (directoryPath == null){
        return null
    }
    val startIndex = directoryPath.indexOf("=")+1
    val endIndex = directoryPath.indexOf(")")

    return directoryPath.slice(startIndex until endIndex).toUri()
}