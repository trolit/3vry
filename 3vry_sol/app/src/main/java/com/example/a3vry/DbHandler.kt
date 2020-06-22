package com.example.a3vry

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.Html
import android.widget.Toast
import androidx.core.text.HtmlCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDate

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

    private fun switchPlaylistArtist() {
        val db = this.writableDatabase
        val query = "SELECT 1 FROM $artist_TABLE_NAME WHERE $artist_COL_NAME='playlist' LIMIT 1;"
        val result = db.rawQuery(query, null)
        if(result.count > 0) {
            // delete playlist from artists
            db.delete(artist_TABLE_NAME, "$artist_COL_NAME=playlist", null)
        } else {
            // insert playlist to artists
            insertBand(Artist("playlist"))
        }
        result.close()
        db.close()
    }

    fun getSongs() : MutableList<Song> {
        val list : MutableList<Song> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $song_TABLE_NAME"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()) {
            do {
                val song = Song()
                song.id = result.getString(0).toInt()
                song.title = result.getString(1).toString()
                song.dateTime = result.getString(2).toString()
                song.url = result.getString(3).toString()
                song.artistId = result.getString(4).toInt()
                list.add(song)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list.asReversed()
    }

    fun checkForNewSong() {
        var currentDate = LocalDate.now()
        // currentDate = LocalDate.parse("2020-06-22")
        val db = this.readableDatabase
        val query = "SELECT EXISTS (SELECT * FROM $song_TABLE_NAME WHERE $song_COL_DATETIME='$currentDate' LIMIT 1);"
        val cursor : Cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        println("Current date => $currentDate")
        println("SSS => $cursor")
        // if true, there is already song assigned to that date
        if(cursor.getInt(0) == 1) {
            // do nothing
        } else {
            // 1. Get artists
            val result = getArtists()
            if(result.count() > 0) {
                // 2. Get random artist
                result.shuffle()
                val artist = result[0]

                // 3. Make query to youtube & insert song
                queryForVideo(artist.name, artist.id)
            } else {
                Toast.makeText(context, "Artists not found!", Toast.LENGTH_SHORT).show()
            }
        }
        cursor.close()
        db.close()
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

    private fun queryForVideo(artistName : String, artistId : Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(YouTubeApiService.YOUTUBE_SEARCH_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val service = retrofit.create(YouTubeApiService::class.java)

        if(artistName != "playlist") {
            val searchCall = service.results(artistName)
            searchCall?.enqueue(object : Callback<YoutubeGetResponse> {
                override fun onResponse(call: Call<YoutubeGetResponse>, response: Response<YoutubeGetResponse>) {
                    if (response.isSuccessful){
                        if (response.body()?.items?.count()!! > 0) {
                            val song = response.body()?.items?.random()
                            val title = Html.fromHtml(song!!.snippet!!.title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                            val songObj = Song(title, LocalDate.now().toString(), song.id!!.videoId, artistId)
                            insertSong(songObj)
                        }
                    }
                }
                override fun onFailure(call: Call<YoutubeGetResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        } else if (artistName == "playlist") {
            val searchCall = service.playlistResults()
            searchCall?.enqueue(object : Callback<YoutubeGetPlaylistResponse> {
                override fun onResponse(call: Call<YoutubeGetPlaylistResponse>, response: Response<YoutubeGetPlaylistResponse>) {
                    if (response.isSuccessful){
                        if (response.body()?.items?.count()!! > 0) {
                            val song = response.body()?.items?.random()
                            val title = Html.fromHtml(song!!.snippet!!.title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                            val songObj = Song(title, LocalDate.now().toString(), song.snippet!!.resourceId!!.videoId, artistId)
                            insertSong(songObj)
                        }
                    }
                }
                override fun onFailure(call: Call<YoutubeGetPlaylistResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

    }
}