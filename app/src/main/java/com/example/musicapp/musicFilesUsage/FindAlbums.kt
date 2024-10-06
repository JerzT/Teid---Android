package com.example.musicapp.musicFilesUsage

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay



fun findAlbums(
    uri: Uri?,
    context: Context,
    listOfAlbums: MutableList<Album>,
): Deferred<Unit> = GlobalScope.async{
    //return when uri is null
    if(uri == null){
        return@async
    }

    //Checking every files in given directory
    val documentFile = DocumentFile.fromTreeUri(context, uri)

    if (documentFile != null && documentFile.isDirectory) {
        val files = documentFile.listFiles()
        for (file in files) {
            if (file.isDirectory){
                //go inside if is a directory
                findAlbums(file.uri, context, listOfAlbums).await()
            }
            else{
                //check if folder contains audio files and if true makes album
                if(file.type?.contains("audio") == true){
                    val metadata = getMetadata(file, context)
                    val album = Album(
                        name = metadata["albumName"] ?: documentFile.name,
                        uri = documentFile.uri,
                        cover = getCover(documentFile),
                        artist = metadata["artistName"],
                        year = metadata["albumYear"],
                    )
                    listOfAlbums += album
                    return@async
                }
            }
        }
    }
}

fun getMetadata(file: DocumentFile, context: Context): Map<String?, String?> {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(context, file.uri)

        val albumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        val artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        val albumYear = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)

        mapOf(
            "albumName" to albumName,
            "artistName" to artistName,
            "albumYear" to albumYear,
        )
    } catch (e: Exception) {
        mapOf()
    } finally {
        retriever.release()
    }
}

fun getCover(directory: DocumentFile): Uri?{
    var uri: Uri? = null

    if (directory.isDirectory){
        for(file in directory.listFiles()){
            if (file.type?.contains("image") == true){
                uri = file.uri
                return uri
            }
        }
    }

    return uri
}