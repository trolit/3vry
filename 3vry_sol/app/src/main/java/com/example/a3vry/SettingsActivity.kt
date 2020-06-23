package com.example.a3vry

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_artists.*

import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.backToMainMenuBtn

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
