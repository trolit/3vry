package com.example.a3vry

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import kotlinx.android.synthetic.main.activity_playlist.*
import kotlinx.android.synthetic.main.activity_settings.backToMainMenuBtn
import kotlinx.android.synthetic.main.add_playlist_dialog.view.*

class PlaylistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        backToMainMenuBtn.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // SET ADAPTER AND ADD NEW PLAYLIST BTN
        val db = DbHandler(this)
        val playlists = db.getPlaylists()

        val adapter = Playlist_ListAdapter(this, R.layout.playlists_list_item, playlists)
        playlist_List.adapter = adapter

        playlistDialogToggleBtn.setOnClickListener {
            // inflate dialog with custom view
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_playlist_dialog, null)
            // alert dialog builder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("New playlist")
                .setMessage(HtmlCompat.fromHtml(this.getString(R.string.addPlaylistHint), HtmlCompat.FROM_HTML_MODE_LEGACY))
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
                } else {
                    Toast.makeText(this, this.getString(R.string.missingPlaylistId), Toast.LENGTH_SHORT).show()
                }
            }
            // handle bandDialogCancelBtn
            mDialogView.playlistDialogCancelBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }
}
