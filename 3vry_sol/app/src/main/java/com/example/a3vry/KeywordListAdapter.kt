package com.example.a3vry

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.keywords_list_item.view.*

class KeywordListAdapter(context: Context, var resource: Int, var listOfKeywords: MutableList<Keyword>) :
    ArrayAdapter<Keyword>(context, resource, listOfKeywords) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView?:inflater.inflate(resource, null)

        val textViewKeywordName = view.textViewKeywordName

        val keyword = listOfKeywords[position]

        textViewKeywordName.text = keyword.name

        view.deleteKeywordBtn.setOnClickListener {
            val alertDialogBuilder = buildAlertDialog(context)
            alertDialogBuilder.setPositiveButton(context.getString(R.string.rawYes)) { _: DialogInterface, _: Int ->
                val db = DbHandler(context)
                db.deleteRowFromDb(keyword.id, keywords_TABLE_NAME)
                listOfKeywords.removeAt(position)
                notifyDataSetChanged()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        return view
    }
}