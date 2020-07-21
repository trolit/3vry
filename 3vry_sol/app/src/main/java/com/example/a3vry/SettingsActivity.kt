package com.example.a3vry

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        backToMainMenuBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val db = DbHandler(this)

        viewPlaylistsBtn.setOnClickListener {
            startActivity(Intent(this, PlaylistActivity::class.java))
        }

        viewKeywordsBtn.setOnClickListener {
            startActivity(Intent(this, KeywordsActivity::class.java))
        }

        // SETUP WIPE SONGS BUTTON
        val result = db.checkIfTableContainsTwoSongs()
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

        // SETUP PLAYLIST
        val isPlaylistEnabled = db.checkIfPlaylistIsEnabled()
        if(isPlaylistEnabled) {
            disablePlaylistBtn.isVisible = true
            setStatusOnTextView(playlistStatusTextView, enabled, greenColor)
        } else {
            enablePlaylistBtn.isVisible = true
            setStatusOnTextView(playlistStatusTextView, disabled, redColor)
        }
        disablePlaylistBtn.setOnClickListener {
            db.removeAuthorPlaylist()
            swapButtons(enablePlaylistBtn, disablePlaylistBtn)
            setStatusOnTextView(playlistStatusTextView, disabled, redColor)
        }
        enablePlaylistBtn.setOnClickListener {
            db.addAuthorPlaylist()
            swapButtons(enablePlaylistBtn, disablePlaylistBtn)
            setStatusOnTextView(playlistStatusTextView, enabled, greenColor)
        }

        // SETUP VIDEO RANGE AND VIDEO DURATION DISPLAY DATA
        val currentVr = db.getPrefValue(videoRange)
        setCurrentValueOnTextView(searchingRangeHeader, this.getString(R.string.rangeSettingHeader), currentVr)

        val currentVd = db.getPrefValue(videoDuration)
        setCurrentValueOnTextView(videoDurationHeader, this.getString(R.string.videoDurationHeader), currentVd)
        
        changeSearchRangeBtn.setOnClickListener {
            val options = arrayOf<CharSequence>("50", "100", "150", "200")
            val builder = AlertDialog.Builder(this)
            builder.setTitle(this.getString(R.string.selectOption))
            builder.setItems(options) { _, outcome ->
                when (outcome) {
                    0 -> {
                        db.updatePreference(Preference(videoRange, "50"))
                        setCurrentValueOnTextView(searchingRangeHeader, this.getString(R.string.rangeSettingHeader), "50")
                    }
                    1 -> {
                        db.updatePreference(Preference(videoRange, "100"))
                        setCurrentValueOnTextView(searchingRangeHeader, this.getString(R.string.rangeSettingHeader), "100")
                    }
                    2 -> {
                        db.updatePreference(Preference(videoRange, "150"))
                        setCurrentValueOnTextView(searchingRangeHeader, this.getString(R.string.rangeSettingHeader), "150")
                    }
                    3 -> {
                        db.updatePreference(Preference(videoRange, "200"))
                        setCurrentValueOnTextView(searchingRangeHeader, this.getString(R.string.rangeSettingHeader), "200")
                    }
                }
            }
            builder.show()
        }
        changeVideoDurationBtn.setOnClickListener {
            val options = arrayOf<CharSequence>("short (less than 4 minutes)", "medium (4 - 20 minutes)", "long (more than 20 minutes)")
            val builder = AlertDialog.Builder(this)
            builder.setTitle(this.getString(R.string.selectOption))
            builder.setItems(options) { _, outcome ->
                when (outcome) {
                    0 -> {
                        db.updatePreference(Preference(videoDuration, "short"))
                        setCurrentValueOnTextView(videoDurationHeader, this.getString(R.string.videoDurationHeader), "short")
                    }
                    1 -> {
                        db.updatePreference(Preference(videoDuration, "medium"))
                        setCurrentValueOnTextView(videoDurationHeader, this.getString(R.string.videoDurationHeader), "medium")
                    }
                    2 -> {
                        db.updatePreference(Preference(videoDuration, "long"))
                        setCurrentValueOnTextView(videoDurationHeader, this.getString(R.string.videoDurationHeader), "long")
                    }
                }
            }
            builder.show()
        }
    }

    private fun setCurrentValueOnTextView(textView: TextView, text: String, value: String) {
        textView.text = HtmlCompat.fromHtml("$text <strong>$value</strong>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setStatusOnTextView(textView: TextView, status: String, color: String) {
        textView.text = HtmlCompat.fromHtml("<font color='$color'>$status</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun swapButtons(btn1: ImageView, btn2: ImageView) {
        btn1.isVisible = !btn1.isVisible
        btn2.isVisible = !btn2.isVisible
    }

    companion object {
        const val videoRange = "videoRange"
        const val videoDuration = "videoDuration"
        const val enabled = "ENABLED"
        const val disabled = "DISABLED"
        const val greenColor = "#16A085"
        const val redColor = "#9F001C"
    }
}
