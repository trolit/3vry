package com.example.a3vry

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_playlist.*
import kotlinx.android.synthetic.main.activity_settings.backToMainMenuBtn
import kotlinx.android.synthetic.main.add_playlist_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PlaylistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        backToMainMenuBtn.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Build Retrofit2 service
        val retrofit = Retrofit.Builder()
            .baseUrl(YouTubeApiService.YOUTUBE_SEARCH_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val service = retrofit.create(YouTubeApiService::class.java)

        // SET ADAPTER AND ADD NEW PLAYLIST BTN
        val db = DbHandler(this)
        val playlists = db.getPlaylists()

        if(playlists.count() <= 0) {
            emptyListMessage.isVisible = true
        }

        val adapter = PlaylistListAdapter(this, R.layout.playlists_list_item, playlists)
        playlist_List.adapter = adapter

        playlistDialogToggleBtn.setOnClickListener {
            // inflate dialog with custom view
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_playlist_dialog, null)
            // alert dialog builder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("New playlist")
                .setMessage(HtmlCompat.fromHtml(this.getString(R.string.addPlaylistHint), HtmlCompat.FROM_HTML_MODE_LEGACY))

            mDialogView.validationResultDialogTextView.isVisible = false

            // show dialog
            val mAlertDialog = mBuilder.show()
            // handle bandDialogAddBtn
            mDialogView.playlistDialogAddBtn.setOnClickListener {
                mAlertDialog.dismiss()
                // get data
                val name = mDialogView.playlistDialogName.text.toString()
                if(name.isNotEmpty()) {
                    val playlist = Playlist(name)
                    db.insertPlaylist(playlist)
                    adapter.add(playlist)
                    adapter.notifyDataSetChanged()
                    if(emptyListMessage.isVisible) {
                        emptyListMessage.isVisible = false
                    }
                } else {
                    Toast.makeText(this, this.getString(R.string.missingPlaylistId), Toast.LENGTH_SHORT).show()
                }
            }
            mDialogView.validatePlaylistIdDialogBtn.setOnClickListener {
                val playlistId = mDialogView.playlistDialogName.text.toString()
                if(playlistId.isNotEmpty()) {
                    mDialogView.validationResultDialogTextView.text ="checking.."
                    isPlaylistIdValid(service, playlistId, mDialogView.validationResultDialogTextView)
                }
            }

            // handle bandDialogCancelBtn
            mDialogView.playlistDialogCancelBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

    private fun isPlaylistIdValid(service: YouTubeApiService, playlistId: String, viewValidationResultTextEdit: TextView) {
        val searchCall = service.validatePlaylist(playlistId)
        searchCall?.enqueue(object : Callback<YoutubeGetPlaylistResponse> {
            override fun onResponse(call: Call<YoutubeGetPlaylistResponse>, response: Response<YoutubeGetPlaylistResponse>) {
                viewValidationResultTextEdit.isVisible = true
                if (response.isSuccessful){
                    viewValidationResultTextEdit.text =
                        HtmlCompat.fromHtml(getString(R.string.validPlaylistId), HtmlCompat.FROM_HTML_MODE_LEGACY)
                } else if (response.errorBody()!!.string().contains("parameter cannot be found")) {
                    viewValidationResultTextEdit.text =
                        HtmlCompat.fromHtml(getString(R.string.invalidPlaylistId), HtmlCompat.FROM_HTML_MODE_LEGACY)
                }
            }
            override fun onFailure(call: Call<YoutubeGetPlaylistResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}
