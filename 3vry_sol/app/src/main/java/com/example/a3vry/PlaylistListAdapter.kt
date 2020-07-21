package com.example.a3vry

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.playlists_list_item.view.*

class PlaylistListAdapter(context: Context, var resource: Int, var listOfPlaylists: MutableList<Playlist>) :
    ArrayAdapter<Playlist>(context, resource, listOfPlaylists) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView?:inflater.inflate(resource, null)

        val textViewPlaylistName = view.textViewPlaylistName

        val playlist = listOfPlaylists[position]

        textViewPlaylistName.text = playlist.playlistId

        if(playlist.playlistId == appAuthorPlaylist) {
            view.deletePlaylistBtn.isVisible = false
        } else {
            view.deletePlaylistBtn.isVisible = true
            view.deletePlaylistBtn.setOnClickListener {
                val alertDialogBuilder = buildAlertDialog(context)
                alertDialogBuilder.setPositiveButton(context.getString(R.string.rawYes)) { _: DialogInterface, _: Int ->
                    val db = DbHandler(context)
                    db.deleteRowFromDb(playlist.id, playlists_TABLE_NAME)
                    listOfPlaylists.removeAt(position)
                    notifyDataSetChanged()
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }
        return view
    }
}