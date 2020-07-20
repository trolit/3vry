package com.example.a3vry

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.playlists_list_item.view.*

class Playlist_ListAdapter(context: Context, var resource: Int, var listOfPlaylists: MutableList<Playlist>) :
    ArrayAdapter<Playlist>(context, resource, listOfPlaylists) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView?:inflater.inflate(resource, null)

        val textViewPlaylistName = view.textViewPlaylistName

        val playlist = listOfPlaylists[position]

        textViewPlaylistName.text = playlist.playlistId

        view.deletePlaylistBtn.setOnClickListener {
            removeItem(context, position, playlist.id, playlists_TABLE_NAME, listOfPlaylists)
            notifyDataSetChanged()
        }

        return view
    }
}