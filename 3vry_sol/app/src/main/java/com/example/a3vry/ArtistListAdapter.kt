package com.example.a3vry

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.artists_list_item.view.*

class ArtistListAdapter(context: Context, var resource: Int, var artistList: MutableList<Artist>) :
    ArrayAdapter<Artist>(context, resource, artistList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView?:inflater.inflate(resource, null)

        val textViewBandName = view.textViewBandName

        val artist = artistList[position]

        textViewBandName.text = artist.name

        view.deleteBandBtn.setOnClickListener {
            removeItem(position, artist.id)
        }

        if(artist.name == "playlist") {
            view.deleteBandBtn.isVisible = false
        }
        return view
    }

    private fun removeItem(pos: Int, artistId: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Are you sure you want to delete?")

        alertDialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener { _: DialogInterface, i: Int ->
            val db = DbHandler(context)
            db.deleteRowFromDb(artistId, artist_TABLE_NAME)
            artistList.removeAt(pos)
            notifyDataSetChanged()
        })
        alertDialogBuilder.setNegativeButton("No", DialogInterface.OnClickListener { _: DialogInterface, i: Int ->
            // empty
        })

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}