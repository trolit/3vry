package com.example.a3vry

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewArtistsBtn.setOnClickListener {
            startActivity(Intent(this, ArtistsActivity::class.java))
        }
        viewSongsBtn.setOnClickListener {
            intentSongsActivity()
        }
        viewSettingsBtn.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        buttonWhat.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(HtmlCompat.fromHtml(this.getString(R.string.appDefinition), HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setPositiveButton(HtmlCompat.fromHtml(this.getString(R.string.htmlOk), HtmlCompat.FROM_HTML_MODE_LEGACY)
                ) { _, _ ->
                    // dismiss
                }
            builder.create()
            builder.show()
        }
        buttonWhy.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(HtmlCompat.fromHtml(this.getString(R.string.appReason), HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setPositiveButton(HtmlCompat.fromHtml(this.getString(R.string.htmlOk), HtmlCompat.FROM_HTML_MODE_LEGACY)
                ) { _, _ ->
                    // dismiss
                }
            builder.create()
            builder.show()
        }

        val db = DbHandler(this)
        val isArtistsTableNotEmpty = db.checkIfTableContainsAtLeastOneObject("Artists")
        val isSongsTableNotEmpty = db.checkIfTableContainsAtLeastOneObject("Songs")

        if(isArtistsTableNotEmpty) {
            db.checkForNewSong()
        } else if(!isArtistsTableNotEmpty && !isSongsTableNotEmpty) {
            // override viewArtistsBtn functionality
            viewSongsBtn.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(HtmlCompat.fromHtml(this.getString(R.string.noArtistsAndSongsMessage), HtmlCompat.FROM_HTML_MODE_LEGACY))
                    .setPositiveButton(HtmlCompat.fromHtml(this.getString(R.string.htmlOk), HtmlCompat.FROM_HTML_MODE_LEGACY)
                    ) { _, _ ->
                        // dismiss
                    }
                    .setNegativeButton(HtmlCompat.fromHtml(this.getString(R.string.takeMeAnyway), HtmlCompat.FROM_HTML_MODE_LEGACY)
                    ) { _, _ ->
                        intentSongsActivity()
                    }
                builder.create()
                builder.show()
            }
        } else if(!isArtistsTableNotEmpty && isSongsTableNotEmpty) {
            viewSongsBtn.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(HtmlCompat.fromHtml(this.getString(R.string.noArtistsMessage), HtmlCompat.FROM_HTML_MODE_LEGACY))
                    .setPositiveButton(HtmlCompat.fromHtml(this.getString(R.string.htmlOk), HtmlCompat.FROM_HTML_MODE_LEGACY)
                    ) { _, _ ->
                        intentSongsActivity()
                    }
                builder.create()
                builder.show()
            }
        }
        db.close()
    }

    private fun intentSongsActivity() {
        val intent = Intent(this, SongsActivity::class.java)
        startActivity(intent)
    }
}
