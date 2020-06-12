package com.example.a3vry

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_artists.*
import kotlinx.android.synthetic.main.add_artist_dialog.view.*


class ArtistsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artists)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var db = DbHandler(this)

        var artists = db.getArtists()

        var adapter = ArtistListAdapter(this, R.layout.artists_list_item, artists)
        bandsList.adapter = adapter

        bandDialogToggleBtn.setOnClickListener {
            // inflate dialog with custom view
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_artist_dialog, null)
            // alert dialog builder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Artist name")
            // show dialog
            val mAlertDialog = mBuilder.show()
            // handle bandDialogAddBtn
            mDialogView.bandDialogAddBtn.setOnClickListener {
                mAlertDialog.dismiss()
                // get data
                val name = mDialogView.bandDialogName.text.toString()
                if(name.isNotEmpty()) {
                    var artist = Artist(name)
                    db.insertBand(artist)
                    adapter.add(artist)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Please fill in artist name", Toast.LENGTH_SHORT).show()
                }
            }
            // handle bandDialogCancelBtn
            mDialogView.bandDialogCancelBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }
}
