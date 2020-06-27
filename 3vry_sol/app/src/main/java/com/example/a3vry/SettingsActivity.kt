package com.example.a3vry

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

        // SETUP WIPE SONGS BUTTON
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
            db.removePlaylistAsArtist()
            swapButtons(enablePlaylistBtn, disablePlaylistBtn)
            setStatusOnTextView(playlistStatusTextView, disabled, redColor)
        }
        enablePlaylistBtn.setOnClickListener {
            db.addPlaylistAsArtist()
            swapButtons(enablePlaylistBtn, disablePlaylistBtn)
            setStatusOnTextView(playlistStatusTextView, enabled, greenColor)
        }

        // SETUP ACOUSTIC SETTING
        setupPrefSetting(db, enableAcousticBtn, disableAcousticBtn, includeAcoustic, greenColor, redColor,
            this.getString(R.string.acousticEnabled), this.getString(R.string.acousticDisabled), acousticStatusTextView)

        // SETUP COVER SETTING
        setupPrefSetting(db, enableCoversBtn, disableCoversBtn, includeCovers, greenColor, redColor,
            this.getString(R.string.coverEnabled), this.getString(R.string.coverDisabled), coverStatusTextView)

        // SETUP VIDEO RANGE AND VIDEO DURATION SETTINGS
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

    private fun setupPrefSetting(db: DbHandler, enableBtn: ImageView, disableBtn: ImageView,
                                 prefKey: String, positiveColor: String, negativeColor: String,
                                 enableMsg: String, disableMsg: String, headerTextView: TextView) {

        val currentPrefSetting = db.getPrefValue(prefKey)

        // Setup UI
        if(currentPrefSetting == "disabled") {
            enableBtn.isVisible = true
            setStatusOnTextView(headerTextView, disabled, negativeColor)
        } else if(currentPrefSetting == "enabled") {
            disableBtn.isVisible = true
            setStatusOnTextView(headerTextView, enabled, positiveColor)
        }

        // Setup Buttons
        enableBtn.setOnClickListener {
            db.updatePreference(Preference(includeCovers, "enabled"))
            setStatusOnTextView(headerTextView, enabled, positiveColor)
            swapButtons(enableBtn, disableBtn)
            Toast.makeText(this, enableMsg, Toast.LENGTH_SHORT).show()
        }
        disableBtn.setOnClickListener {
            db.updatePreference(Preference(includeCovers, "disabled"))
            setStatusOnTextView(headerTextView, disabled, negativeColor)
            swapButtons(enableBtn, disableBtn)
            Toast.makeText(this, disableMsg, Toast.LENGTH_SHORT).show()
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
        const val includeCovers = "includeCovers"
        const val includeAcoustic = "includeAcoustic"
    }
}
