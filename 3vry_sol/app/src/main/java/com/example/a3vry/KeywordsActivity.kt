package com.example.a3vry

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_keywords.*
import kotlinx.android.synthetic.main.activity_settings.backToMainMenuBtn
import kotlinx.android.synthetic.main.add_keyword_dialog.view.*

class KeywordsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keywords)

        backToMainMenuBtn.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // SETUP ADAPTER AND ADD NEW PLAYLIST BTN
        val db = DbHandler(this)
        val keywords = db.getKeywords()

        if(keywords.count() <= 0) {
            emptyListMessage.isVisible = true
        }

        val adapter = KeywordListAdapter(this, R.layout.keywords_list_item, keywords)
        keywordList.adapter = adapter

        keywordDialogToggleBtn.setOnClickListener {
            // inflate dialog with custom view
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_keyword_dialog, null)
            // alert dialog builder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("New keyword")
                .setMessage("Fill in below input with keyword you would like to be ignored.")
            // show dialog
            val mAlertDialog = mBuilder.show()
            // handle bandDialogAddBtn
            mDialogView.keywordDialogAddBtn.setOnClickListener {
                mAlertDialog.dismiss()
                // get data
                val name = mDialogView.keywordDialogName.text.toString()
                if(name.isNotEmpty()) {
                    val keyword = Keyword(name)
                    db.insertKeyword(keyword)
                    adapter.add(keyword)
                    adapter.notifyDataSetChanged()
                    if(emptyListMessage.isVisible) {
                        emptyListMessage.isVisible = false
                    }
                } else {
                    Toast.makeText(this, this.getString(R.string.missingKeywordName), Toast.LENGTH_SHORT).show()
                }
            }
            // handle bandDialogCancelBtn
            mDialogView.keywordDialogCancelBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }
}
