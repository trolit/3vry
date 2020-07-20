package com.example.a3vry

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

        if(artist.name == "playlist") {
            view.deleteBandBtn.isVisible = false
        } else {
            view.deleteBandBtn.setOnClickListener {
                val alertDialogBuilder = buildAlertDialog(context)
                alertDialogBuilder.setPositiveButton(context.getString(R.string.rawYes)) { _: DialogInterface, _: Int ->
                    val db = DbHandler(context)
                    db.deleteRowFromDb(artist.id, artist_TABLE_NAME)
                    artistList.removeAt(position)
                    notifyDataSetChanged()
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }
        return view
    }
}