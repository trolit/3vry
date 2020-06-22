package com.example.a3vry

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_songs.*

class SongsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

interface YouTubeApiService {
    // Get results by query
    @GET("search?key=$YOUTUBE_KEY_API&part=snippet&type=video&maxResults=50&topicId=/m/04rlf&videoCategoryId=10&order=viewCount&videoDuration=short")
    fun results(@Query("q") q: String?): Call<YoutubeGetResponse>?
    // Get results from app author playlist
    @GET("playlistItems?key=$YOUTUBE_KEY_API&part=snippet&maxResults=50&playlistId=PLCrKXyV2OjXi2VF42Dimxvv9fnOH3JEl6")
    fun playlistResults(): Call<YoutubeGetPlaylistResponse>?

    // ********************************************************************
    // BEFORE RUNNING 3VRY, MAKE SURE TO FILL IN YOUTUBE_KEY_API BELOW !! *
    // ********************************************************************
    companion object {
        const val YOUTUBE_SEARCH_BASE_URL = "https://www.googleapis.com/youtube/v3/"
    }
}
