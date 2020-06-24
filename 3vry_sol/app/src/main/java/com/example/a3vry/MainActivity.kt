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
            val intent = Intent(this, ArtistsActivity::class.java)
            startActivity(intent)
        }

        viewSongsBtn.setOnClickListener {
            val intent = Intent(this, SongsActivity::class.java)
            startActivity(intent)
        }

        viewSettingsBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        buttonWhat.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val text = "<font color='#16A085'>What is 3vry?</font><br/><br/><small>" +
                    "Open source application created by <font color='#16A085'>github.com/trolit</font> using Kotlin " +
                    "language that allows user to add favourite artists and get randomly single track offer each day from " +
                    "YouTube. It's also possible to include app author playlist(visit Settings section).<br/><br/>3vry requires " +
                    "Internet permission to make requests to YouTube service. App does not gather any personal data and source " +
                    "code is available at:<br/><br/><font color='#16A085'>https://github.com/trolit/3vry</font></small>"

            builder.setMessage(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setPositiveButton(HtmlCompat.fromHtml("<font color='#16A085'>Ok</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                ) { _, _ ->
                    // dismiss
                }
            builder.create()
            builder.show()
        }

        buttonWhy.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val text = "<font color='#16A085'>Why 3vry?</font><br/><br/><small>" +
                    "By making 3vry I mainly wanted to meet Kotlin and IntelliJ IDEA IDE after Xamarin.Forms project but " +
                    "also make an app that will encourage to listen to at least one track each day. Listening to music has " +
                    "powerful impact on human mind. Music is very crucial in games and movies. Maybe with this tool you will " +
                    "find track(s) that you did not hear previously and get another favourite song(s)? Enjoy :)<br/><br/> " +
                    "~Paweł Idzikowski(trolit)<br/>23.06.2020</small>"
            builder.setMessage(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setPositiveButton(HtmlCompat.fromHtml("<font color='#16A085'>Ok</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                ) { _, _ ->
                    // dismiss
                }
            builder.create()
            builder.show()
        }

        val db = DbHandler(this)

        val result = db.checkIfTableContainsAtLeastOneObject("Artists")
        if(result) {
            db.checkForNewSong()
        } else {
            // override viewArtistsBtn functionality
            viewSongsBtn.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                val text = "<font color='#16A085'>Before you enter..</font><br/><br/><small>" +
                        "songs section, please note that the list will be empty. Add at least one artist or enable app author " +
                        " playlist in settings section in order to receive your first track. </small>"
                builder.setMessage(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
                    .setPositiveButton(HtmlCompat.fromHtml("<font color='#16A085'>Ok</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                    ) { _, _ ->
                        // dismiss
                    }
                    .setNegativeButton(HtmlCompat.fromHtml("Take me anyway", HtmlCompat.FROM_HTML_MODE_LEGACY)
                    ) { _, _ ->
                        val intent = Intent(this, SongsActivity::class.java)
                        startActivity(intent)
                    }
                builder.create()
                builder.show()
            }
        }
        db.close()
    }
}
