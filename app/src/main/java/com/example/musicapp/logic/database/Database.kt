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
import com.example.musicapp.logic.artist.Artist
import com.example.musicapp.logic.song.Song

fun setUpDatabase(context: Context): DBHelper {
    val database = DBHelper(context, null)
    database.getAlbums()

    return database
}

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val queryArtist = """
            CREATE TABLE $ARTISTS (
                $ID_COL INTEGER PRIMARY KEY,
                $NAME_COL TEXT,
                $COVER_COL TEXT,
                $URI_COL TEXT
            )
        """.trimIndent()
        db.execSQL(queryArtist)

        val queryAlbums ="""
            CREATE TABLE $ALBUMS (
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
        db.execSQL("DROP TABLE IF EXISTS $ARTISTS")
        db.execSQL("DROP TABLE IF EXISTS $ALBUMS")
        db.execSQL("DROP TABLE IF EXISTS $SONGS")
        onCreate(db)
    }

    @SuppressLint("Recycle")
    fun addArtist(artist: Artist){
        val db = this.writableDatabase

        val name = (artist.name ?: "")
        val cover = (artist.cover ?: "").toString()
        val coverAlbum = artist.coverAlbum.toString()

        try {
            val query = " SELECT * FROM $ARTISTS " +
                    "WHERE $NAME_COL = ? "
            val cursor = db.rawQuery(query,
                arrayOf(name))
            if (cursor.moveToFirst()) {
                Log.v("DBHelper", "Artist already exists: $name")
            } else {
                val values = ContentValues().apply {
                    put(NAME_COL, name)
                    put(COVER_COL, cover)
                    put(URI_COL, coverAlbum)
                }
                db.insert(ARTISTS, null, values)
                Log.v("DBHelper", "Artist added successfully: $name")
            }
        }
        catch (e: Exception){
            Log.e("DBHelper", "Error while adding artist: ${e.message}", e)
        }
        finally {
            db.close()
        }
    }

    fun getArtists(): Cursor {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM $ARTISTS", null)
    }

    @SuppressLint("Recycle")
    fun addAlbum(album: Album) {
        val db = this.writableDatabase

        val name = album.name ?: ""
        val uri = album.uri.toString()
        val cover = if(album.cover != null) album.cover.toString() else ""
        val artist = album.artist ?: ""
        val year = album.year ?: ""
        val cdNumber = album.cdNumber ?: 1

        try {
            val query = "SELECT * FROM $ALBUMS " +
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
                val values = ContentValues().apply {
                    put(NAME_COL, name)
                    put(URI_COL, uri)
                    put(COVER_COL, cover)
                    put(ARTIST_COL, artist)
                    put(YEAR_COL, year)
                    put(CD_NUMBER_COL, cdNumber)
                }

                db.insert(ALBUMS, null, values)
                Log.v("DBHelper", "Album added successfully: $name")
            }
        } catch (e: Exception) {
            Log.e("DBHelper", "Error while adding album: ${e.message}", e)
        } finally {
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

            db.delete( ALBUMS, whereClause, whereArgs)
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

        return db.rawQuery("SELECT * FROM $ALBUMS", null)
    }

    fun getAlbumFromUri(parentUri: Uri): Cursor{
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM $ALBUMS WHERE $URI_COL = ?", arrayOf(parentUri.toString()))
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
            val query = "SELECT * FROM $SONGS " +
                    "WHERE $TITLE_COL = ? " +
                    "AND $URI_COL = ? " +
                    "AND $PARENT_URI_COL = ? " +
                    "AND $ARTIST_COL = ?" +
                    "AND $FORMAT_COL = ? " +
                    "AND $NUMBER_COL = ? " +
                    "AND $LENGTH_COL = ? " +
                    "AND $TIME_PLAYED_COL = ?"
            val cursor = db.rawQuery(query,
                arrayOf(title, uri.toString(), parentUri.toString(), artist, format, "$number", "$length", "$timePlayed"))

            if (cursor.moveToFirst()) {
                Log.v("DBHelper", "Song already exists: $title")
            } else {
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
                Log.v("DBHelper", "Song added successfully: $title")
            }
        }
        catch (e: Exception){
            Log.e("DBHelper", "Error while adding song: ${e.message}", e)
        }
        finally {
            db.close()
        }
    }

    fun deleteSong(song: Song){
        val db = this.writableDatabase

        try {
            val whereClause =
                    "$TITLE_COL = ? " +
                    "AND $URI_COL = ? " +
                    "AND $PARENT_URI_COL = ? " +
                    "AND $ARTIST_COL = ? " +
                    "AND $FORMAT_COL = ? " +
                    "AND $NUMBER_COL = ? " +
                    "AND $LENGTH_COL = ? " +
                    "AND $TIME_PLAYED_COL = ?"

            val whereArgs = arrayOf(
                song.title ?: "",
                song.uri.toString(),
                song.parentUri.toString(),
                song.author ?: "",
                song.format ?: "",
                song.number.toString(),
                song.length.toString(),
                song.timePlayed.toString()
            )

            db.delete( SONGS, whereClause, whereArgs)
        }
        catch (e: Exception){
            Log.e("DBHelper", "Error while deleting song: ${e.message}", e)
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
        const val ARTISTS = "artist"
        const val ALBUMS = "albums"
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