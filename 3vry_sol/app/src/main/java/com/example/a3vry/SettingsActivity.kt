package com.example.a3vry

import android.content.Intent
import android.os.Bundle
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

        val greenColor = "#16A085"
        val redColor = "#9F001C"

        backToMainMenuBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
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
            setStatusOnPlaylistTextView(this.getString(R.string.enabled), greenColor)
        } else {
            enablePlaylistBtn.isVisible = true
            setStatusOnPlaylistTextView(this.getString(R.string.disabled), redColor)
        }

        disablePlaylistBtn.setOnClickListener {
            db.removePlaylistAsArtist()
            swapButtons()
            setStatusOnPlaylistTextView(this.getString(R.string.disabled), redColor)
        }
        enablePlaylistBtn.setOnClickListener {
            db.addPlaylistAsArtist()
            swapButtons()
            setStatusOnPlaylistTextView(this.getString(R.string.enabled), greenColor)
        }

        val currentVr = db.getPrefValue(videoRange)
        setStatusOnTextView(searchingRangeHeader, this.getString(R.string.rangeSettingHeader), currentVr)

        val currentVd = db.getPrefValue(videoDuration)
        setStatusOnTextView(videoDurationHeader, this.getString(R.string.videoDurationHeader), currentVd)

        changeSearchRangeBtn.setOnClickListener {
            val options = arrayOf<CharSequence>("50", "100", "150", "200")
            val builder = AlertDialog.Builder(this)
            builder.setTitle(this.getString(R.string.selectOption))
            builder.setItems(options) { _, outcome ->
                when (outcome) {
                    0 -> {
                        db.updatePreference(Preference(videoRange, "50"))
                        setStatusOnTextView(searchingRangeHeader, this.getString(R.string.rangeSettingHeader), "50")
                    }
                    1 -> {
                        db.updatePreference(Preference(videoRange, "100"))
                        setStatusOnTextView(searchingRangeHeader, this.getString(R.string.rangeSettingHeader), "100")
                    }
                    2 -> {
                        db.updatePreference(Preference(videoRange, "150"))
                        setStatusOnTextView(searchingRangeHeader, this.getString(R.string.rangeSettingHeader), "150")
                    }
                    3 -> {
                        db.updatePreference(Preference(videoRange, "200"))
                        setStatusOnTextView(searchingRangeHeader, this.getString(R.string.rangeSettingHeader), "200")
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
                        setStatusOnTextView(videoDurationHeader, this.getString(R.string.videoDurationHeader), "short")
                    }
                    1 -> {
                        db.updatePreference(Preference(videoDuration, "medium"))
                        setStatusOnTextView(videoDurationHeader, this.getString(R.string.videoDurationHeader), "medium")
                    }
                    2 -> {
                        db.updatePreference(Preference(videoDuration, "long"))
                        setStatusOnTextView(videoDurationHeader, this.getString(R.string.videoDurationHeader), "long")
                    }
                }
            }
            builder.show()
        }
    }

    private fun setStatusOnTextView(textView: TextView, text: String, value: String) {
        textView.text = HtmlCompat.fromHtml("$text <strong>$value</strong>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setStatusOnPlaylistTextView(status: String, color: String) {
        playlistStatusTextView.text = HtmlCompat.fromHtml("<font color='$color'>$status</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun swapButtons() {
        disablePlaylistBtn.isVisible = !disablePlaylistBtn.isVisible
        enablePlaylistBtn.isVisible = !enablePlaylistBtn.isVisible
    }

    companion object {
        const val videoRange = "videoRange"
        const val videoDuration = "videoDuration"
    }
}
