package com.example.musicapp.logic.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.util.Log
import com.example.musicapp.logic.album.Album
import com.example.musicapp.logic.song.Song

fun setUpDatabase(context: Context): DBHelper {
    val database = DBHelper(context, null)
    database.getAlbums()

    return database
}

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val queryAlbums ="""
            CREATE TABLE $MUSIC_ALBUMS (
                $ID_COL INTEGER PRIMARY KEY,
                $NAME_COL TEXT,
                $URI_COL TEXT UNIQUE, 
                $COVER_COL TEXT,
                $ARTIST_COL TEXT,
                $YEAR_COL INTEGER, 
                $CD_NUMBER_COL INTEGER
            )""".trimIndent()
        db.execSQL(queryAlbums)

        val querySongs = """
            CREATE TABLE $SONGS (
                $ID_COL INTEGER PRIMARY KEY,
                $TITLE_COL TEXT,
                $URI_COL TEXT UNIQUE,
                $PARENT_URI_COL TEXT,
                $ARTIST_COL TEXT,
                $FORMAT_COL TEXT,
                $NUMBER_COL INTEGER,
                $LENGTH_COL INTEGER, 
                $TIME_PLAYED_COL INTEGER
            )""".trimIndent()

        db.execSQL(querySongs)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS $MUSIC_ALBUMS")
        db.execSQL("DROP TABLE IF EXISTS $SONGS")
        onCreate(db)
    }

    @SuppressLint("Recycle")
    fun addAlbum(album: Album) {
        val db = this.writableDatabase

        // Safeguard against null values and check them
        val name = album.name ?: ""
        val uri = album.uri.toString()
        val cover = if(album.cover != null) album.cover.toString() else ""
        val artist = album.artist ?: ""
        val year = album.year ?: ""
        val cdNumber = album.cdNumber ?: 1

        try {
            // Parameterized query to check if the album already exists in the database
            val query = "SELECT * FROM $MUSIC_ALBUMS " +
                    "WHERE $NAME_COL = ? " +
                    "AND $URI_COL = ? " +
                    "AND $COVER_COL = ? " +
                    "AND $ARTIST_COL = ? " +
                    "AND $YEAR_COL = ?" +
                    "AND $CD_NUMBER_COL = ?"
            val cursor = db.rawQuery(query,
                arrayOf(name, uri, cover, artist, year, cdNumber.toString()))

            if (cursor.moveToFirst()) {
                // Album already exists
                Log.v("DBHelper", "Album already exists: $name")
            } else {
                // Insert album if it does not exist
                val values = ContentValues().apply {
                    put(NAME_COL, name)
                    put(URI_COL, uri)
                    put(COVER_COL, cover)
                    put(ARTIST_COL, artist)
                    put(YEAR_COL, year)
                    put(CD_NUMBER_COL, cdNumber)
                }

                db.insert(MUSIC_ALBUMS, null, values)
                Log.v("DBHelper", "Album added successfully: $name")
            }
        } catch (e: Exception) {
            // Catch any exceptions and log them
            Log.e("DBHelper", "Error while adding album: ${e.message}", e)
        } finally {
            // Close the database connection
            db.close()
        }
    }

    fun deleteAlbum(album: Album){
        val db = this.writableDatabase

        try {
            val whereClause = "$NAME_COL = ? " +
                    "AND $URI_COL = ? " +
                    "AND $COVER_COL = ? " +
                    "AND $ARTIST_COL = ? " +
                    "AND $YEAR_COL = ? " +
                    "AND $CD_NUMBER_COL = ?"
            val whereArgs = arrayOf(
                album.name ?: "",
                album.uri.toString(),
                if(album.cover != null) album.cover.toString() else "",
                album.artist ?: "",
                album.year ?: "",
                album.cdNumber.toString()
            )

            db.delete( MUSIC_ALBUMS, whereClause, whereArgs)
        }
        catch (e: Exception){
            Log.e("DBHelper", "Error while deleting album: ${e.message}", e)
        }
        finally {
            db.close()
        }
    }

    fun getAlbums(): Cursor {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM $MUSIC_ALBUMS", null)
    }

    @SuppressLint("Recycle")
    fun addSong(song: Song){
        val db = this.writableDatabase

        val uri = song.uri
        val parentUri = song.parentUri
        val title = song.title ?: ""
        val artist = song.author ?: ""
        val format = song.format ?: ""
        val number = song.number
        val length = song.length
        val timePlayed = song.timePlayed

        try {
            // Parameterized query to check if the album already exists in the database
            val query = "SELECT * FROM $SONGS " +
                    "WHERE $TITLE_COL = ? " +
                    "AND $URI_COL = ? " +
                    "AND $PARENT_URI_COL = ? " +
                    "AND $ARTIST_COL = ?" +
                    "AND $FORMAT_COL = ? " +
                    "AND $NUMBER_COL = ? " +
                    "AND $LENGTH_COL = ?" +
                    "AND $TIME_PLAYED_COL = ?"
            val cursor = db.rawQuery(query,
                arrayOf(title, uri.toString(), parentUri.toString(), artist, format, "$number", "$length", "$timePlayed"))

            if (cursor.moveToFirst()) {
                // Album already exists
                Log.v("DBHelper", "Album already exists: $title")
            } else {
                // Insert album if it does not exist
                val values = ContentValues().apply {
                    put(TITLE_COL, title)
                    put(URI_COL, uri.toString())
                    put(PARENT_URI_COL, parentUri.toString())
                    put(ARTIST_COL, artist)
                    put(FORMAT_COL, format)
                    put(NUMBER_COL, number)
                    put(LENGTH_COL, length)
                    put(TIME_PLAYED_COL, timePlayed)
                }

                db.insert(SONGS, null, values)
                Log.v("DBHelper", "Album added successfully: $title")
            }
        }
        catch (e: Exception){
            Log.e("DBHelper", "Error while adding song: ${e.message}", e)
        }
        finally {
            db.close()
        }
    }

    fun getSongs(): Cursor{
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM $SONGS", null)
    }

    fun getSongsWithUri(parentUri: Uri): Cursor{
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM $SONGS WHERE $PARENT_URI_COL = ?", arrayOf(parentUri.toString()))
    }

    companion object{
        private const val DATABASE_NAME = "APP_DATABASE"

        private const val DATABASE_VERSION = 1

        const val MUSIC_ALBUMS = "music_albums"

        const val ID_COL = "id"

        const val NAME_COL = "name"

        const val URI_COL = "uri"

        const val COVER_COL = "cover"

        const val ARTIST_COL = "artist"

        const val YEAR_COL = "year"

        const val CD_NUMBER_COL = "cd_number"

        const val SONGS = "songs"

        const val TITLE_COL = "title"

        const val PARENT_URI_COL = "parent_uri"

        const val FORMAT_COL = "format"

        const val NUMBER_COL = "number"

        const val LENGTH_COL = "length"

        const val TIME_PLAYED_COL = "time_played"
    }
}