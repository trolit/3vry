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
const val song_COL_BANDID = "bandId"

const val band_TABLE_NAME = "Bands"
const val band_COL_NAME = "name"

class DbHandler (var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    // Runs when device does not contain Database
    override fun onCreate(db: SQLiteDatabase?) {
        val createBandsTable = "CREATE TABLE " + band_TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                band_COL_NAME + " VARCHAR(256)" +
                ");"

        db?.execSQL(createBandsTable);

        val createSongsTable = "CREATE TABLE " + song_TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                song_COL_TITLE + " VARCHAR(256)," +
                song_COL_DATETIME + " VARCHAR(50)," +
                song_COL_URL + " VARCHAR(256)," +
                song_COL_BANDID + " INTEGER REFERENCES " + band_TABLE_NAME + "(" + COL_ID + "));"

        db?.execSQL(createSongsTable);
    }

    // Executed when device holds older version of Database
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertSong(song: Song) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_ID, song.id)
        cv.put(song_COL_TITLE, song.title)
        cv.put(song_COL_DATETIME, song.dateTime)
        cv.put(song_COL_URL, song.url)
        cv.put(song_COL_BANDID, song.bandId)
        var result = db.insert(song_TABLE_NAME, null, cv)
        if(result == (-1).toLong()) {
            Toast.makeText(context, "Song insertion failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success adding song", Toast.LENGTH_SHORT).show()
        }
    }

    fun insertBand(band: Band) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_ID, band.id)
        cv.put(band_COL_NAME, band.name)
        var result = db.insert(band_TABLE_NAME, null, cv)
        if(result == -1.toLong()) {
            Toast.makeText(context, "Error, band not added", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Band added", Toast.LENGTH_SHORT).show()
        }
    }
}