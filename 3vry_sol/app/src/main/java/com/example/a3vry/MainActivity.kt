package com.example.a3vry

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
                    "~Paweł Idzikowski(trolit), 23.06.2020</small>"
            builder.setMessage(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setPositiveButton(HtmlCompat.fromHtml("<font color='#16A085'>Ok</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                ) { _, _ ->
                    // dismiss
                }
            builder.create()
            builder.show()
        }

        val db = DbHandler(this)
        val result = db.checkIfDbContainsAtLeastOneArtist()
        if(result) {
            db.checkForNewSong()
        } else {
            // override viewArtistsBtn functionality
            viewSongsBtn.setOnClickListener {

            }
        }
        db.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
