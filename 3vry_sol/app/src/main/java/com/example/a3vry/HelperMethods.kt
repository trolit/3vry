package com.example.a3vry

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

fun <E>removeItem(context: Context, pos: Int, rowId: Int, tableName: String, list: MutableList<E>) {
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setTitle(context.getString(R.string.confirmDeleteOperation))
    alertDialogBuilder.setPositiveButton(context.getString(R.string.rawYes)) { _: DialogInterface, _: Int ->
        val db = DbHandler(context)
        db.deleteRowFromDb(rowId, tableName)
        list.removeAt(pos)
    }
    alertDialogBuilder.setNegativeButton(context.getString(R.string.rawNo)) { _: DialogInterface, _: Int ->
        // empty
    }
    val alertDialog = alertDialogBuilder.create()
    alertDialog.show()
}