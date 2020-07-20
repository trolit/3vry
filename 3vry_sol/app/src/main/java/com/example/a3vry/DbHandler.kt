package com.example.a3vry

import android.content.ContentValues
import android.content.Context
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val DATABASE_NAME = "EvryDb"

const val COL_ID = "id"

const val song_TABLE_NAME = "Songs"
const val song_COL_TITLE = "title"
const val song_COL_DATETIME = "dateTime"
const val song_COL_URL = "url"
const val song_COL_ARTISTID = "artistId"

const val artist_TABLE_NAME = "Artists"
const val artist_COL_NAME = "name"

const val preferences_TABLE_NAME = "Preferences"
const val preferences_COL_PARAMETER = "parameter"
const val preferences_COL_VALUE = "value"

const val keywords_TABLE_NAME = "Keywords"
const val keywords_COL_NAME = "name"

const val playlists_TABLE_NAME = "Playlists"
const val playlists_COL_PLAYLISTID = "playlistId"

const val playlist = "playlist"

const val appAuthorPlaylist = "PLCrKXyV2OjXi2VF42Dimxvv9fnOH3JEl6"

class DbHandler (var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    // Runs when device does not contain Database
    override fun onCreate(db: SQLiteDatabase?) {
        val createBandsTable = "CREATE TABLE $artist_TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$artist_COL_NAME VARCHAR(256)" +
                ");"

        db?.execSQL(createBandsTable)

        val createSongsTable = "CREATE TABLE " + song_TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                song_COL_TITLE + " VARCHAR(256)," +
                song_COL_DATETIME + " VARCHAR(50)," +
                song_COL_URL + " VARCHAR(256)," +
                song_COL_ARTISTID + " INTEGER REFERENCES " + artist_TABLE_NAME + "(" + COL_ID + "));"  // !!! NOT ON DELETE CASCADE!

        db?.execSQL(createSongsTable)

        val createParametersTable = "CREATE TABLE $preferences_TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$preferences_COL_PARAMETER VARCHAR(50)," +
                "$preferences_COL_VALUE VARCHAR(50)" +
                ");"

        db?.execSQL(createParametersTable)

        val addDefaultPrefs = "INSERT INTO $preferences_TABLE_NAME " +
                "($preferences_COL_PARAMETER, $preferences_COL_VALUE) " +
                "VALUES " +
                "('videoRange', '150'), " +
                "('videoDuration', 'short'), " +
                "('includeCovers','disabled'), " +
                "('includeAcoustic', 'disabled'), " +
                "('includeLive', 'disabled');"

        db?.execSQL(addDefaultPrefs)

        val createPlaylistsTable = "CREATE TABLE $playlists_TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$playlists_COL_PLAYLISTID VARCHAR(150));"

        db?.execSQL(createPlaylistsTable)

        val createKeywordsTable = "CREATE TABLE $keywords_TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$keywords_COL_NAME VARCHAR(70));"

        db?.execSQL(createKeywordsTable)

        val addDefaultKeywords = "INSERT INTO $keywords_TABLE_NAME " +
                "($keywords_COL_NAME) " +
                "VALUES " +
                "('acoustic'), " +
                "('live'), " +
                "('parody'), " +
                "('cover'), " +
                "('fan');"

        db?.execSQL(addDefaultKeywords)
    }

    // Executed when device holds older version of Database
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    // **************************************************************
    // PREFERENCES TABLE OPERATIONS
    // **************************************************************

    fun updatePreference(preference: Preference) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(preferences_COL_PARAMETER, preference.parameter)
        cv.put(preferences_COL_VALUE, preference.value)
        val result = db.update(preferences_TABLE_NAME, cv, "$preferences_COL_PARAMETER=?",
            arrayOf(preference.parameter)).toLong()
        if(result <= 0 ) {
            Toast.makeText(context, context.getString(R.string.failedPrefInsertion), Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun getPrefValue(parameter: String) : String {
        val db = this.readableDatabase
        val columns = arrayOf(preferences_COL_VALUE)
        val selection = "$preferences_COL_PARAMETER=?"
        val selectionArgs = arrayOf(parameter)
        val limit = "1"
        val result = db.query(preferences_TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit)
        result.moveToFirst()
        val outcome = result.getString(0)
        result.close()
        db.close()
        return outcome
    }

    // **************************************************************
    // PLAYLISTS TABLE OPERATIONS
    // **************************************************************

    fun getPlaylists() : MutableList<Playlist> {
        val list : MutableList<Playlist> = ArrayList()
        val db = this.readableDatabase
        val result = db.query(playlists_TABLE_NAME, null, null, null, null, null, null, null)

        if(result.moveToFirst()) {
            do {
                val playlist = Playlist()
                playlist.id = result.getString(0).toInt()
                playlist.playlistId = result.getString(1).toString()
                list.add(playlist)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun insertPlaylist(playlist: Playlist) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(playlists_COL_PLAYLISTID, playlist.playlistId)
        val result = db.insert(playlists_TABLE_NAME, null, cv)
        if(result == (-1).toLong()) {
            Toast.makeText(context, context.getString(R.string.failedPlaylistInsertion), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, context.getString(R.string.succededPlaylistInsertion), Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun addAuthorPlaylist() {
        insertPlaylist(Playlist(appAuthorPlaylist))
        Toast.makeText(context, context.getString(R.string.playlistEnabled), Toast.LENGTH_SHORT).show()
    }

    fun removeAuthorPlaylist() {
        val db = this.writableDatabase
        val res = db.delete(playlists_TABLE_NAME, "$playlists_COL_PLAYLISTID='$appAuthorPlaylist'", null).toLong()
        if(res != (-1).toLong()) {
            Toast.makeText(context, context.getString(R.string.playlistDisabled), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, context.getString(R.string.playlistNotDisabled), Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    // **************************************************************
    // KEYWORDS TABLE OPERATIONS
    // **************************************************************

    fun getKeywords() : MutableList<Keyword> {
        val list : MutableList<Keyword> = ArrayList()
        val db = this.readableDatabase
        val result = db.query(keywords_TABLE_NAME, null, null, null, null, null, null, null)

        if(result.moveToFirst()) {
            do {
                val keyword = Keyword()
                keyword.id = result.getString(0).toInt()
                keyword.name = result.getString(1).toString()
                list.add(keyword)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun insertKeyword(keyword: Keyword) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(keywords_COL_NAME, keyword.name)
        val result = db.insert(keywords_TABLE_NAME, null, cv)
        if(result == (-1).toLong()) {
            Toast.makeText(context, context.getString(R.string.failedKeywordInsertion), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, context.getString(R.string.succededKeywordInsertion), Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    // **************************************************************
    // ARTISTS TABLE OPERATIONS
    // **************************************************************

    fun insertBand(artist: Artist) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(artist_COL_NAME, artist.name)
        val result = db.insert(artist_TABLE_NAME, null, cv)
        if(result == (-1).toLong()) {
            Toast.makeText(context, context.getString(R.string.failedArtistInsertion), Toast.LENGTH_SHORT).show()
        } else if(artist.name != playlist) {
            Toast.makeText(context, context.getString(R.string.succededArtistInsertion), Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun getArtists() : MutableList<Artist> {
        val list : MutableList<Artist> = ArrayList()
        val db = this.readableDatabase
        val result = db.query(artist_TABLE_NAME, null, null, null, null, null, null, null)

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

    fun checkIfPlaylistIsEnabled() : Boolean {
        val db = this.readableDatabase
        val selection = "$artist_COL_NAME=?"
        val selectionArgs = arrayOf(playlist)
        val limit = "1"
        val result = db.query(artist_TABLE_NAME, null, selection, selectionArgs, null, null, null, limit)
        if(result.count > 0) {
            result.close()
            db.close()
            return true
        }
        result.close()
        db.close()
        return false
    }

    // **************************************************************
    // SONGS TABLE OPERATIONS
    // **************************************************************

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
            Toast.makeText(context, context.getString(R.string.failedSongInsertion), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, context.getString(R.string.succededSongInsertion), Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun wipeAllSongs() {
        val db = this.writableDatabase
        val result = db.delete(song_TABLE_NAME, "1", null);
        Toast.makeText(context, "$result " + context.getString(R.string.nTracksRemoved), Toast.LENGTH_SHORT).show()
        db.close()
    }

    fun getSongs() : MutableList<Song> {
        val list : MutableList<Song> = ArrayList()
        val db = this.readableDatabase
        val result = db.query(song_TABLE_NAME, null, null, null, null, null, null, null)
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
        val sdf = SimpleDateFormat("dd-M-yyyy")
        val currentDate = sdf.format(Date())
        val db = this.readableDatabase
        val selection = "$song_COL_DATETIME=?"
        val selectionArgs = arrayOf(currentDate)
        val limit = "1"
        val queryResult = db.query(song_TABLE_NAME, null, selection, selectionArgs, null, null, null, limit)
        queryResult.moveToFirst()
        // if true, there is already song assigned to that date
        if(queryResult.getInt(0) == 1) {
            // do nothing
        } else {
            // 1. Get artists
            val artists = getArtists()
            if(artists.count() > 0) {
                // 2. Get random artist
                val artist = artists.random()
                // 3. Make query to youtube & insert song
                queryForVideo(artist.name, artist.id)
            } else {
                Toast.makeText(context, context.getString(R.string.artistsMissing), Toast.LENGTH_SHORT).show()
            }
        }
        queryResult.close()
        db.close()
    }

    // **************************************************************
    // GENERAL OPERATIONS
    // **************************************************************

    fun checkIfTableContainsAtLeastOneObject(tableName: String) : Boolean {
        val db = this.readableDatabase
        val columns = arrayOf("count(*)")
        val limit = "1"
        val result = db.query(tableName, columns, null, null, null, null, null, limit)
        result.moveToFirst()
        if(result.getInt(0) > 0) {
            result.close()
            db.close()
            return true
        }
        result.close()
        db.close()
        return false
    }

    fun deleteRowFromDb(id: Int, tableName: String) {
        val db = this.writableDatabase
        val result = db.delete(tableName, "$COL_ID=?", arrayOf(id.toString())).toLong()
        db.close()
        if(result == (-1).toLong()) {
            Toast.makeText(context, context.getString(R.string.elementNotRemoved), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, context.getString(R.string.elementRemoved), Toast.LENGTH_SHORT).show()
        }
    }

    // **************************************************************
    // OPERATIONS ON YOUTUBE API
    // **************************************************************

    private fun queryForVideo(artistName : String, artistId : Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(YouTubeApiService.YOUTUBE_SEARCH_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val service = retrofit.create(YouTubeApiService::class.java)

        val sdf = SimpleDateFormat("dd-M-yyyy")
        val currentDate = sdf.format(Date())
        println("Current date => $currentDate")

        val outcome = getPrefValue("videoRange")
        // possible outcomes: 50, 100, 150, 200
        val pageRange = outcome.toInt() / 50
        val targetPage = (1..pageRange).random()

        if(artistName != playlist) {
            val videoDuration = getPrefValue("videoDuration")
            callYoutubeSearch(service, artistName, artistId, "", targetPage, 0, currentDate, videoDuration)
        } else if (artistName == playlist) {
            callYoutubePlaylistSearch(service, artistName, artistId, "", targetPage, 0, currentDate)
        }
    }

    private fun callYoutubePlaylistSearch(service: YouTubeApiService, artistName : String, artistId : Int,
                                          pageToken: String, targetPage: Int, currentIteration: Int, currentDate: String) {
        val searchCall = service.playlistResults(pageToken)
        searchCall?.enqueue(object : Callback<YoutubeGetPlaylistResponse> {
            override fun onResponse(call: Call<YoutubeGetPlaylistResponse>, response: Response<YoutubeGetPlaylistResponse>) {
                if (response.isSuccessful){

                    if (currentIteration != targetPage) {
                        val nextPageToken = response.body()?.nextPageToken.toString()
                        callYoutubePlaylistSearch(service, artistName, artistId, nextPageToken, targetPage, currentIteration + 1, currentDate)
                    } else if (response.body()?.items?.count()!! > 0) {
                        val song = response.body()?.items?.random()
                        val title = Html.fromHtml(song!!.snippet!!.title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                        val songObj = Song(title, currentDate, song.snippet!!.resourceId!!.videoId, artistId)
                        insertSong(songObj)
                    }
                } else if (response.errorBody()!!.string().contains("exceeded")) {
                    Toast.makeText(context, context.getString(R.string.dailyQuotaLimitExceeded), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, context.getString(R.string.songNotAdded), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<YoutubeGetPlaylistResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun callYoutubeSearch(service: YouTubeApiService, artistName : String, artistId : Int, pageToken: String,
                                  targetPage: Int, currentIteration: Int, currentDate: String, videoDuration: String) {
        val searchCall = service.results(artistName, pageToken, videoDuration)
        searchCall?.enqueue(object : Callback<YoutubeGetResponse> {
            override fun onResponse(call: Call<YoutubeGetResponse>, response: Response<YoutubeGetResponse>) {
                if (response.isSuccessful){
                    if (currentIteration != targetPage) {
                        val nextPageToken = response.body()?.nextPageToken.toString()
                        callYoutubeSearch(service, artistName, artistId, nextPageToken,
                            targetPage, currentIteration + 1, currentDate, videoDuration)
                    } else if (response.body()?.items?.count()!! > 0) {
                        val includeCoversResult = getPrefValue("includeCovers")
                        val includeAcousticResult = getPrefValue("includeAcoustic")
                        val includeLiveResult = getPrefValue("includeLive")
                        val song = returnFilteredSong(includeCoversResult, includeAcousticResult,
                            includeLiveResult, response.body()?.items!!)
                        if(song != null) {
                            val title = Html.fromHtml(song.snippet!!.title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                            val songObj = Song(title, currentDate, song.id!!.videoId, artistId)
                            insertSong(songObj)
                        } else {
                            println("****************************************************")
                            println("Song not inserted into the Db, method returned null.")
                            println("****************************************************")
                        }
                    }
                    // println("First song TITLE: " + response.body()?.items?.get(0)?.snippet!!.title)
                } else if (response.errorBody()!!.string().contains("exceeded")) {
                    Toast.makeText(context, context.getString(R.string.dailyQuotaLimitExceeded), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, context.getString(R.string.songNotAdded), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<YoutubeGetResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun returnFilteredSong (coverStatus: String, acousticStatus: String, liveStatus: String,
                                    songList: List<YoutubeSingleItem>) : YoutubeSingleItem? {
        val enabled = "enabled"
        val disabled = "disabled"
        val cover = "cover"
        val acoustic = "acoustic"
        val live = "live"

        if(coverStatus == enabled && acousticStatus == enabled && liveStatus == enabled) {
            return song(songList)
        } else if (coverStatus == disabled && acousticStatus == disabled && liveStatus == disabled) {
            return song(songList, cover, acoustic, live)
        } else if (coverStatus == disabled && acousticStatus == disabled) {
            return song(songList, cover, acoustic)
        } else if (acousticStatus == disabled && liveStatus == disabled) {
            return song(songList, acoustic, live)
        } else if (coverStatus == disabled && liveStatus == disabled) {
            return song(songList, cover, live)
        } else if (coverStatus == disabled) {
            return song(songList, cover)
        } else if (acousticStatus == disabled) {
            return song(songList, acoustic)
        } else if (liveStatus == disabled) {
            return song(songList, liveStatus)
        }
        return null
    }

    private val karaoke = "karaoke" // remove karaoke phrase from song search
    private fun song(songList: List<YoutubeSingleItem>) : YoutubeSingleItem {
        var song : YoutubeSingleItem
        do {
            song = songList.random()
        } while (song.snippet!!.title.contains(karaoke, ignoreCase = true))
        return song
    }
    private fun song(songList: List<YoutubeSingleItem>, p1: String) : YoutubeSingleItem {
        var song : YoutubeSingleItem
        do {
            song = songList.random()
        } while (song.snippet!!.title.contains(p1, ignoreCase = true) ||
            song.snippet!!.title.contains(karaoke, ignoreCase = true))
        return song
    }
    private fun song(songList: List<YoutubeSingleItem>, p1: String, p2: String) : YoutubeSingleItem {
        var song : YoutubeSingleItem
        do {
            song = songList.random()
        } while (song.snippet!!.title.contains(p1, ignoreCase = true) ||
            song.snippet!!.title.contains(p2, ignoreCase = true) ||
            song.snippet!!.title.contains(karaoke, ignoreCase = true))
        return song
    }
    private fun song(songList: List<YoutubeSingleItem>, p1: String, p2: String, p3: String) : YoutubeSingleItem {
        var song : YoutubeSingleItem
        do {
            song = songList.random()
        } while (song.snippet!!.title.contains(p1, ignoreCase = true) ||
            song.snippet!!.title.contains(p2, ignoreCase = true) ||
            song.snippet!!.title.contains(p3, ignoreCase = true) ||
            song.snippet!!.title.contains(karaoke, ignoreCase = true))
        return song
    }
}