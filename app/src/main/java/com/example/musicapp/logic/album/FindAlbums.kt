package com.example.musicapp.logic.album

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import com.example.musicapp.logic.database.setUpDatabase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
fun findAlbums(
    uri: Uri?,
    context: Context,
    albumsList: MutableList<Any>,
): Deferred<Unit> = GlobalScope.async{
    val database = setUpDatabase(context)

    if(uri == null){
        return@async
    }

    val supportedAudioFormats = listOf(
        "mp3", "flac", "m4a",
        "aac", "wav", "ogg",
        "amr", "mid", "xmf",
        "mxmf", "rtttl", "rtx",
        "ota", "imy", "3gp",
        "ts", "mkv", "wv", "mpeg")

    //Checking every files in given directory
    val documentFile = DocumentFile.fromTreeUri(context, uri)

    if (documentFile != null && documentFile.isDirectory) {
        val files = documentFile.listFiles()
        coroutineScope {
            for (file in files) {
                if (file.isDirectory){
                    launch {
                        findAlbums(file.uri, context, albumsList).await()
                    }
                }
                else{

                    //check if folder contains audio files and if true makes album
                    if(file.type?.let { type ->
                            supportedAudioFormats.any{
                                type.substringAfter("/", "") == it}} == true
                        )
                    {
                        val metadata = getMetadata(file, context)
                        val album = Album(
                            name = metadata["albumName"] ?: documentFile.name,
                            uri = documentFile.uri,
                            cover = getCover(documentFile),
                            artist = metadata["artistName"],
                            year = metadata["albumYear"],
                            cdNumber = metadata["cdNumber"]?.toInt(),
                        )
                        albumsList.add(album)
                        database.addAlbum(album)
                        return@coroutineScope
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
private fun getMetadata(file: DocumentFile, context: Context): Map<String?, String?> {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(context, file.uri)

        val albumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)?.trim()
        val artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)?.trim()
            ?: retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)?.trim()
            ?: retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR)?.trim()
            ?: retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER)?.trim()
        val albumYear = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)?.trim()
        var cdNumber = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER)?.trim()
            ?: "1"

        if(cdNumber.contains("/")){
            cdNumber = cdNumber.split("/")[0]
        }

        mapOf(
            "albumName" to albumName,
            "artistName" to artistName,
            "albumYear" to albumYear,
            "cdNumber" to cdNumber,
        )
    } catch (e: Exception) {
        mapOf()
    } finally {
        retriever.release()
    }
}

private fun getCover(directory: DocumentFile): Uri? {
    val imageFile = directory.listFiles().firstOrNull { file ->
        file.type?.contains("image", ignoreCase = true) == true
    }
    return imageFile?.uri
}