package com.example.a3vry

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.band_list_item.view.*

class BandListAdapter(context: Context, var resource: Int, var bandList: MutableList<Band>) :
    ArrayAdapter<Band>(context, resource, bandList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView?:inflater.inflate(resource, null)

        var textViewBandName = view.textViewBandName

        var band = bandList.get(position)

        textViewBandName.setText(band.name)

        view.deleteBandBtn.setOnClickListener {
            removeItem(position)
        }
        return view
    }

    private fun removeItem(pos: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Are you sure you want to delete?")

        alertDialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener { _: DialogInterface, i: Int ->
            bandList.removeAt(pos)
            notifyDataSetChanged()
        })
        alertDialogBuilder.setNegativeButton("No", DialogInterface.OnClickListener { _: DialogInterface, i: Int ->
            // empty
        })

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}