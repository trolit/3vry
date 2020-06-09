package com.example.a3vry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_bands.*
import kotlinx.android.synthetic.main.activity_bands.view.*
import kotlinx.android.synthetic.main.add_band_dialog.view.*

class BandsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bands)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // fab.setOnClickListener { view ->
        //    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //        .setAction("Action", null).show()
        // }

        bandDialogToggleBtn.setOnClickListener {
            // inflate dialog with custom view
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_band_dialog, null)
            // alert dialog builder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Band name?")
            // show dialog
            val mAlertDialog = mBuilder.show()
            // handle bandDialogAddBtn
            mDialogView.bandDialogAddBtn.setOnClickListener {
                mAlertDialog.dismiss()
                // get data
                val name = mDialogView.bandDialogName.text.toString()
                // TODO: check if no duplicate
                if(name.isNotEmpty()) {
                    var band = Band(name)
                    var db = DbHandler(this)
                    db.insertBand(band)
                } else {
                    Toast.makeText(this, "Please fill all data", Toast.LENGTH_SHORT).show()
                }
            }
            // handle bandDialogCancelBtn
            mDialogView.bandDialogCancelBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }
}
