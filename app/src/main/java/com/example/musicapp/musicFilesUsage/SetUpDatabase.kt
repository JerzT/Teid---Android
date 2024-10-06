package com.example.musicapp.musicFilesUsage

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

fun setUpDatabase(context: Context): DBHelper{
    val database = DBHelper(context, null)
    database.getAlbums()

    return database
}

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query =
            ("CREATE TABLE " + TABLE_NAME +
                    " (" + ID_COL + " INTEGER PRIMARY KEY, "
                    + NAME_COL + " TEXT,"
                    + URI_COL + " TEXT,"
                    + COVER_COL + " TEXT,"
                    + ARTIST_COL + " TEXT,"
                    + YEAR_COL + " TEXT" +")")
        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    //testing function
    fun openDatabase(){
        val db = this.readableDatabase
    }

    fun addAlbum(album: Album) {
        val db = this.writableDatabase


        // Safeguard against null values and check them
        val name = album.name ?: ""
        val uri = album.uri.toString()
        val cover = album.cover?.toString() ?: ""
        val artist = album.artist ?: ""
        val year = album.year ?: ""

        try {
            // Parameterized query to check if the album already exists in the database
            val query = "SELECT * FROM $TABLE_NAME " +
                    "WHERE $NAME_COL = ? " +
                    "AND $URI_COL = ? " +
                    "AND $COVER_COL = ? " +
                    "AND $ARTIST_COL = ? " +
                    "AND $YEAR_COL = ?"
            val cursor = db.rawQuery(query, arrayOf(name, uri, cover, artist, year))

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
                }

                db.insert(TABLE_NAME, null, values)
                Log.v("DBHelper", "Album added successfully: $name")
            }

            // Ensure the cursor is closed to avoid memory leaks
            cursor.close()
        } catch (e: Exception) {
            // Catch any exceptions and log them
            Log.e("DBHelper", "Error while adding album: ${e.message}", e)
        } finally {
            // Close the database connection
            db.close()
        }
    }

    // below method is to get
    // all data from our database
    fun getAlbums(): Cursor? {

        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val db = this.readableDatabase

        // below code returns a cursor to
        // read data from the database
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

    }

    companion object{
        private const val DATABASE_NAME = "APP_DATABASE"

        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "music_albums"

        const val ID_COL = "id"

        const val NAME_COL = "name"

        const val URI_COL = "uri"

        const val COVER_COL = "cover"

        const val ARTIST_COL = "artist"

        const val YEAR_COL = "year"
    }
}