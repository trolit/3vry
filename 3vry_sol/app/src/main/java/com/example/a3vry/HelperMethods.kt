package com.example.a3vry

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import org.json.JSONObject

fun buildAlertDialog(context: Context) : AlertDialog.Builder {
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setTitle(context.getString(R.string.confirmDeleteOperation))

    alertDialogBuilder.setNegativeButton(context.getString(R.string.rawNo)) { _: DialogInterface, _: Int ->
        // empty
    }

    return alertDialogBuilder
}

fun tryToReturnApiErrorMessage(context: Context, errorBodyAsString: String) {
    try {
        val jObjError = JSONObject(errorBodyAsString)
        Toast.makeText(context, jObjError.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
    }
}