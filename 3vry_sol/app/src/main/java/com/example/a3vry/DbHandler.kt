package com.example.a3vry

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

const val DATABASE_NAME = "EvryDb"

const val COL_ID = "id"

const val song_TABLE_NAME = "Songs"
const val song_COL_TITLE = "title"
const val song_COL_DATETIME = "dateTime"
const val song_COL_URL = "url"
const val song_COL_ARTISTID = "artistId"

const val artist_TABLE_NAME = "Artists"
const val artist_COL_NAME = "name"

class DbHandler (var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    // Runs when device does not contain Database
    override fun onCreate(db: SQLiteDatabase?) {
        val createBandsTable = "CREATE TABLE $artist_TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$artist_COL_NAME VARCHAR(256)" +
                ");"

        db?.execSQL(createBandsTable);

        val createSongsTable = "CREATE TABLE " + song_TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                song_COL_TITLE + " VARCHAR(256)," +
                song_COL_DATETIME + " VARCHAR(50)," +
                song_COL_URL + " VARCHAR(256)," +
                song_COL_ARTISTID + " INTEGER REFERENCES " + artist_COL_NAME + "(" + COL_ID + "));"

        db?.execSQL(createSongsTable);
    }

    // Executed when device holds older version of Database
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertSong(song: Song) {
        val db = this.writableDatabase
        val cv = ContentValues()
        if(song.title.length >= 62) {   // max 62 characters, if reached, add 3 dots
            song.title = song.title.take(62) + "..."
        }
        cv.put(song_COL_TITLE, song.title)
        cv.put(song_COL_DATETIME, song.dateTime)
        cv.put(song_COL_URL, song.url)
        cv.put(song_COL_ARTISTID, song.artistId)
        val result = db.insert(song_TABLE_NAME, null, cv)
        if(result == (-1).toLong()) {
            Toast.makeText(context, "Song insertion failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success adding song", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun insertBand(artist: Artist) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(artist_COL_NAME, artist.name)
        var result = db.insert(artist_TABLE_NAME, null, cv)
        if(result == (-1).toLong()) {
            Toast.makeText(context, "Error occured, artist not added!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Artist added correctly!", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun getArtists() : MutableList<Artist> {
        var list : MutableList<Artist> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $artist_TABLE_NAME"
        var result = db.rawQuery(query, null)

        if(result.moveToFirst()) {
            do {
                var artist = Artist()
                artist.id = result.getString(0).toInt()
                artist.name = result.getString(1).toString()
                list.add(artist)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun deleteRowFromDb(id: Int, tableName: String) {
        val db = this.writableDatabase
        val result = db.delete(tableName, "$COL_ID=?", arrayOf(id.toString())).toLong()
        db.close()
        if(result == (-1).toLong()) {
            Toast.makeText(context, "Error, artist not removed.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Artist removed from the app.", Toast.LENGTH_SHORT).show()
        }
    }
}