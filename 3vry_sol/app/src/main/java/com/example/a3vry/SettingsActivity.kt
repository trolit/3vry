package com.example.a3vry

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val greenColor = "#16A085"
        val redColor = "#9F001C"

        backToMainMenuBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val db = DbHandler(this)

        val result = db.checkIfTableContainsAtLeastOneObject("Songs")
        if(result) {
            wipeSongsBtn.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(HtmlCompat.fromHtml(this.getString(R.string.songWipeConfirmation), HtmlCompat.FROM_HTML_MODE_LEGACY))
                    .setPositiveButton(HtmlCompat.fromHtml(this.getString(R.string.htmlYes), HtmlCompat.FROM_HTML_MODE_LEGACY)
                    ) { _, _ ->
                        db.wipeAllSongs()
                        wipeSongsBtn.isVisible = false
                    }
                    .setNegativeButton(HtmlCompat.fromHtml(this.getString(R.string.htmlNo), HtmlCompat.FROM_HTML_MODE_LEGACY)) { _,_ ->
                        // dismiss alert dialog
                    }
                builder.create()
                builder.show()
            }
        } else {
            wipeSongsBtn.isVisible = false
        }

        val isPlaylistEnabled = db.checkIfPlaylistIsEnabled()
        if(isPlaylistEnabled) {
            disablePlaylistBtn.isVisible = true
            setStatusOnTextView(this.getString(R.string.enabled), greenColor)
        } else {
            enablePlaylistBtn.isVisible = true
            setStatusOnTextView(this.getString(R.string.disabled), redColor)
        }

        disablePlaylistBtn.setOnClickListener {
            db.removePlaylistAsArtist()
            swapButtons()
            setStatusOnTextView(this.getString(R.string.disabled), redColor)
        }

        enablePlaylistBtn.setOnClickListener {
            db.addPlaylistAsArtist()
            swapButtons()
            setStatusOnTextView(this.getString(R.string.enabled), greenColor)
        }
    }

    private fun setStatusOnTextView(status: String, color: String) {
        playlistStatusTextView.text = HtmlCompat.fromHtml("<font color='$color'>$status</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun swapButtons() {
        disablePlaylistBtn.isVisible = !disablePlaylistBtn.isVisible
        enablePlaylistBtn.isVisible = !enablePlaylistBtn.isVisible
    }
}
