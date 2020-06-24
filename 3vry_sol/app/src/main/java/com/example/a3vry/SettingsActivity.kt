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
                val text = "<font color='#9F001C'>Warning!</font><br/><br/><small>" +
                        "This operation removes all songs listed in the application. After clicking YES you cannot undo that " +
                        "operation. Are you sure? </small>"
                builder.setMessage(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
                    .setPositiveButton(HtmlCompat.fromHtml("<font color='#9F001C'>YES</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                    ) { _, _ ->
                        db.wipeAllSongs()
                    }
                    .setNegativeButton(HtmlCompat.fromHtml("<font color='#000000'>NO</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)) { _,_ ->
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
            setStatusOnTextView("ENABLED", greenColor)
        } else {
            enablePlaylistBtn.isVisible = true
            setStatusOnTextView("DISABLED", redColor)
        }

        disablePlaylistBtn.setOnClickListener {
            db.removePlaylistAsArtist()
            swapButtons()
            setStatusOnTextView("DISABLED", redColor)
        }

        enablePlaylistBtn.setOnClickListener {
            db.addPlaylistAsArtist()
            swapButtons()
            setStatusOnTextView("ENABLED", greenColor)
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
