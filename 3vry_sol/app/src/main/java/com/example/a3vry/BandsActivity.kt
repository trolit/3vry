package com.example.a3vry

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bands.*
import kotlinx.android.synthetic.main.add_band_dialog.view.*


class BandsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bands)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var db = DbHandler(this)

        var bands = db.returnBands()

        var adapter = BandListAdapter(this, R.layout.band_list_item, bands)
        bandsList.adapter = adapter

        bandDialogToggleBtn.setOnClickListener {
            // inflate dialog with custom view
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_band_dialog, null)
            // alert dialog builder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Band name")
            // show dialog
            val mAlertDialog = mBuilder.show()
            // handle bandDialogAddBtn
            mDialogView.bandDialogAddBtn.setOnClickListener {
                mAlertDialog.dismiss()
                // get data
                val name = mDialogView.bandDialogName.text.toString()
                if(name.isNotEmpty()) {
                    var band = Band(name)
                    db.insertBand(band)
                    adapter.add(band)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Please fill in band name", Toast.LENGTH_SHORT).show()
                }
            }
            // handle bandDialogCancelBtn
            mDialogView.bandDialogCancelBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }
}
