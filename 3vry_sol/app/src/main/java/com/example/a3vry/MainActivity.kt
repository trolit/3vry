package com.example.a3vry

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.text.HtmlCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Build notification and check for Internet availability
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(returnNotificationChannel())

        if(!checkNetworkState(this)) {
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_wifi)
                .setContentTitle(HtmlCompat.fromHtml(this.getString(R.string.notificationTitle), HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setStyle(NotificationCompat.BigTextStyle().bigText(this.getString(R.string.notificationMsg)))
                .setContentText(this.getString(R.string.notificationMsg))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }

        // Set UI buttons
        viewArtistsBtn.setOnClickListener {
            startActivity(Intent(this, ArtistsActivity::class.java))
        }
        viewSongsBtn.setOnClickListener {
            intentSongsActivity()
        }
        viewSettingsBtn.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        buttonWhat.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(HtmlCompat.fromHtml(this.getString(R.string.appDefinition), HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setPositiveButton(HtmlCompat.fromHtml(this.getString(R.string.htmlOk), HtmlCompat.FROM_HTML_MODE_LEGACY)
                ) { _, _ ->
                    // dismiss
                }
            builder.create()
            builder.show()
        }
        buttonWhy.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(HtmlCompat.fromHtml(this.getString(R.string.appReason), HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setPositiveButton(HtmlCompat.fromHtml(this.getString(R.string.htmlOk), HtmlCompat.FROM_HTML_MODE_LEGACY)
                ) { _, _ ->
                    // dismiss
                }
            builder.create()
            builder.show()
        }

        val db = DbHandler(this)
        val isArtistsTableNotEmpty = db.checkIfTableContainsAtLeastOneObject("Artists")
        val isSongsTableNotEmpty = db.checkIfTableContainsAtLeastOneObject("Songs")

        if(isArtistsTableNotEmpty) {
            db.checkForNewSong()
        } else if(!isArtistsTableNotEmpty && !isSongsTableNotEmpty) {
            // override viewArtistsBtn functionality
            viewSongsBtn.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(HtmlCompat.fromHtml(this.getString(R.string.noArtistsAndSongsMessage), HtmlCompat.FROM_HTML_MODE_LEGACY))
                    .setPositiveButton(HtmlCompat.fromHtml(this.getString(R.string.htmlOk), HtmlCompat.FROM_HTML_MODE_LEGACY)
                    ) { _, _ ->
                        // dismiss
                    }
                    .setNegativeButton(HtmlCompat.fromHtml(this.getString(R.string.takeMeAnyway), HtmlCompat.FROM_HTML_MODE_LEGACY)
                    ) { _, _ ->
                        intentSongsActivity()
                    }
                builder.create()
                builder.show()
            }
        } else if(!isArtistsTableNotEmpty && isSongsTableNotEmpty) {
            viewSongsBtn.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(HtmlCompat.fromHtml(this.getString(R.string.noArtistsMessage), HtmlCompat.FROM_HTML_MODE_LEGACY))
                    .setPositiveButton(HtmlCompat.fromHtml(this.getString(R.string.htmlOk), HtmlCompat.FROM_HTML_MODE_LEGACY)
                    ) { _, _ ->
                        intentSongsActivity()
                    }
                builder.create()
                builder.show()
            }
        }
        db.close()
    }

    private fun intentSongsActivity() {
        val intent = Intent(this, SongsActivity::class.java)
        startActivity(intent)
    }

    // SOURCE: https://stackoverflow.com/questions/45711925/failed-to-post-notification-on-channel-null-target-api-is-26
    private fun returnNotificationChannel() : NotificationChannel {
        val mChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        mChannel.enableLights(true)
        mChannel.lightColor = Color.RED
        mChannel.setShowBadge(false)
        return mChannel
    }

    // SOURCE: https://stackoverflow.com/questions/57284582/networkinfo-has-been-deprecated-by-api-29
    private fun checkNetworkState(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    companion object {
        const val NOTIFICATION_ID = 0
        const val CHANNEL_ID = "CH0"
        const val CHANNEL_NAME = "CH"
    }
}
