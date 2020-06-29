package com.example.a3vry

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.isVisible
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_songs.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


class SongsActivity : YouTubeBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs)

        backToMainMenuBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val db = DbHandler(this)
        val songs = db.getSongs()

        if(songs.count() <= 0) {
            noSongsTextMessage.isVisible = true
        }
        
        val youtubePlayerRef : YouTubePlayer? = null
        val adapter =
            SongListAdapter(this, R.layout.songs_list_item, songs, youtubePlayerView, youtubePlayerRef, closeYoutubeViewBtn)
        
        songList.adapter = adapter
    }
}

interface YouTubeApiService {
    // Get results by query
    @GET("search?key=$YOUTUBE_KEY_API&fields=nextPageToken,items(id(videoId),snippet(title))&part=snippet&type=video&videoSyndicated=true&videoEmbeddable=true&maxResults=50&topicId=/m/04rlf&videoCategoryId=10&order=viewCount")
    fun results(@Query("q") q: String?, @Query("pageToken") pageToken: String?, @Query("videoDuration") videoDuration: String?): Call<YoutubeGetResponse>?
    // Get results from app author playlist
    @GET("playlistItems?key=$YOUTUBE_KEY_API&fields=nextPageToken,items(snippet(title, resourceId(videoId)))&part=snippet&type=video&videoSyndicated=true&videoEmbeddable=true&maxResults=50&playlistId=PLCrKXyV2OjXi2VF42Dimxvv9fnOH3JEl6")
    fun playlistResults(@Query("pageToken") pageToken: String?): Call<YoutubeGetPlaylistResponse>?

    // ********************************************************************
    // BEFORE RUNNING 3VRY, MAKE SURE TO FILL IN YOUTUBE_KEY_API BELOW !! *
    // ********************************************************************
    companion object {
        const val YOUTUBE_SEARCH_BASE_URL = "https://www.googleapis.com/youtube/v3/"
        const val YOUTUBE_KEY_API : String = "FILL THIS PLACE WITH YOUR YOUTUBE API"
    }
}
