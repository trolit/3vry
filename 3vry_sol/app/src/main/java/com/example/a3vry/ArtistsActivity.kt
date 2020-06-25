package com.example.a3vry

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_artists.*
import kotlinx.android.synthetic.main.add_artist_dialog.view.*


class ArtistsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artists)

        backToMainMenuBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val db = DbHandler(this)
        val artists = db.getArtists()

        if(artists.count() <= 0) {
            emptyArtistsListTextView.isVisible = true
        }
        val adapter = ArtistListAdapter(this, R.layout.artists_list_item, artists)
        bandsList.adapter = adapter

        bandDialogToggleBtn.setOnClickListener {
            // inflate dialog with custom view
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_artist_dialog, null)
            // alert dialog builder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("New artist")
            // show dialog
            val mAlertDialog = mBuilder.show()
            // handle bandDialogAddBtn
            mDialogView.bandDialogAddBtn.setOnClickListener {
                mAlertDialog.dismiss()
                // get data
                val name = mDialogView.bandDialogName.text.toString()
                if(name.isNotEmpty()) {
                    val artist = Artist(name)
                    db.insertBand(artist)
                    adapter.add(artist)
                    adapter.notifyDataSetChanged()
                    emptyArtistsListTextView.isVisible = false
                } else {
                    Toast.makeText(this, this.getString(R.string.missingArtistName), Toast.LENGTH_SHORT).show()
                }
            }
            // handle bandDialogCancelBtn
            mDialogView.bandDialogCancelBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }
}
