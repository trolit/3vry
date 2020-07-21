package com.example.a3vry

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.songs_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class SongListAdapter(context: Context, var resource: Int, var songList: MutableList<Song>,
                      var youtubePlayerView: YouTubePlayerView, var youtubePlayerRef: YouTubePlayer?,
                      var youtubeViewCloseBtnRef: FloatingActionButton) :
    ArrayAdapter<Song>(context, resource, songList) {

    lateinit var youtubePlayerInit : YouTubePlayer.OnInitializedListener
    var lastVideoRef : String = "empty"
    var lastPlayBtn : ImageView? = null
    var lastPauseBtn : ImageView? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView?:inflater.inflate(resource, null)

        // Get references
        val textViewDate = view.textViewDate
        val textViewTitle = view.textViewTitle
        val song = songList[position]

        // Overwrite data
        val sdf = SimpleDateFormat("dd-M-yyyy")
        val currentDate = sdf.format(Date())

        if(currentDate == song.dateTime) {
            val tmp = song.dateTime
            textViewDate.text = HtmlCompat.fromHtml("<font color='#16A085'><strong>$tmp</strong></font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            textViewDate.text = song.dateTime
        }
        textViewTitle.text = song.title

        view.playVideoBtn.setOnClickListener {
            // If clicked button while video is paused, resume it
            if(youtubePlayerRef != null && !youtubePlayerRef!!.isPlaying && lastVideoRef == song.url) {
                youtubePlayerRef!!.play()
                swapButtons(view)
            }
            // Else if clicked another video reinitialize sequence
            else if(lastVideoRef != song.url) {
                if(youtubePlayerRef != null) {
                    youtubePlayerRef!!.release()
                    // Reset previous video buttons
                    lastPauseBtn!!.isVisible = false
                    lastPlayBtn!!.isVisible = true
                }
                initYoutubeVideo(song.url)
                lastVideoRef = song.url
                youtubePlayerView.isVisible = true
                youtubePlayerView.initialize(YouTubeApiService.YOUTUBE_KEY_API, youtubePlayerInit)
                youtubeViewCloseBtnRef.show()

                youtubeViewCloseBtnRef.setOnClickListener {
                    youtubePlayerView.isVisible = false
                    youtubePlayerRef!!.release()
                    youtubePlayerRef = null
                    lastVideoRef = "empty"
                    youtubeViewCloseBtnRef.hide()
                    // Reset previous video buttons
                    lastPauseBtn!!.isVisible = false
                    lastPlayBtn!!.isVisible = true
                }

                youtubeViewCloseBtnRef.show()

                swapButtons(view)
                // Store references
                lastPlayBtn = view.playVideoBtn
                lastPauseBtn = view.pauseVideoBtn
            }
        }

        view.pauseVideoBtn.setOnClickListener {
            // If clicked button while playing vid, pause it
            if(youtubePlayerRef != null && youtubePlayerRef!!.isPlaying && lastVideoRef == song.url) {
                youtubePlayerRef!!.pause()
                swapButtons(view)
            }
        }

        return view
    }

    private fun swapButtons(view: View) {
        view.pauseVideoBtn.isVisible = !view.pauseVideoBtn.isVisible
        view.playVideoBtn.isVisible = !view.playVideoBtn.isVisible
    }

    private fun initYoutubeVideo(videoId: String) {
        youtubePlayerInit = object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youtubePlayer: YouTubePlayer?, p2: Boolean) {
                youtubePlayer?.loadVideo(videoId)
                youtubePlayerRef = youtubePlayer!!

                val playbackEventListener = object: PlaybackEventListener {
                    override fun onSeekTo(p0: Int) {
                    }

                    override fun onBuffering(p0: Boolean) {
                    }

                    override fun onPlaying() {
                        lastPauseBtn!!.isVisible = true
                        lastPlayBtn!!.isVisible = false
                    }

                    override fun onStopped() {

                    }

                    override fun onPaused() {
                        lastPauseBtn!!.isVisible = false
                        lastPlayBtn!!.isVisible = true
                    }
                }

                youtubePlayer.setPlaybackEventListener(playbackEventListener)
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                Toast.makeText(context, context.getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show()
            }
        }
    }
}