package com.example.a3vry

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

fun buildAlertDialog(context: Context) : AlertDialog.Builder {
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setTitle(context.getString(R.string.confirmDeleteOperation))

    alertDialogBuilder.setNegativeButton(context.getString(R.string.rawNo)) { _: DialogInterface, _: Int ->
        // empty
    }

    return alertDialogBuilder
}