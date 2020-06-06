package com.example.a3vry

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

const val DATABASE_NAME = "3vry.Db"
val TABLE_NAME = "Songs"
val COL_NAME = "Name"
val COL_ID = "Id"

class DbHandler (var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    // Runs when device does not contain Database
    override fun onCreate(db: SQLiteDatabase?) {
        TODO("Not yet implemented")

        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR(256))";

        db?.execSQL(createTable);
    }

    // Executed when device holds older version of Database
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertSong(song: Song) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_ID, song.Id)
        cv.put(COL_NAME, song.Name)
        var result = db.insert(TABLE_NAME, null, cv)
        if(result == -1.toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }
}