package com.example.musicapp.logic.song

//@SuppressLint("Range")
//fun getSongsFromDatabaseWithUri(
//    context: Context,
//    parentUri: Uri
//): SnapshotStateList<Song> {
//    val database  = setUpDatabase(context)
//
//    val songList: SnapshotStateList<Song> = mutableStateListOf()
//    val getSongsResult = database.getSongsWithUri(parentUri = parentUri)
//
//    getSongsResult.use { songRow ->
//        while (songRow.moveToNext()){
//            val title: String? = if(songRow.getString(songRow.getColumnIndex("title")) != "")
//                songRow.getString(songRow.getColumnIndex("title")) else null
//
//            val uri: Uri = songRow.getString(songRow.getColumnIndex("uri")).toUri()
//
//            val songParentUri: Uri = songRow.getString(songRow.getColumnIndex("parent_uri")).toUri()
//
//            val author: String? = if(songRow.getString(songRow.getColumnIndex("artist")) != "")
//                songRow.getString(songRow.getColumnIndex("artist")) else null
//
//            val format: String? = if(songRow.getString(songRow.getColumnIndex("format")) != "")
//                songRow.getString(songRow.getColumnIndex("format")) else null
//
//            val number: Int = songRow.getInt(songRow.getColumnIndex("number"))
//
//            val length: Int = songRow.getInt(songRow.getColumnIndex("length"))
//
//            val timePlayed: Int = songRow.getInt(songRow.getColumnIndex("time_played"))
//
//            val newSong = Song(
//                title = title,
//                uri = uri,
//                parentUri = songParentUri,
//                author = author,
//                format = format,
//                number = number,
//                length = length,
//                timePlayed = timePlayed,
//            )
//
//            songList += newSong
//        }
//    }
//    getSongsResult.close()
//    return songList
//}