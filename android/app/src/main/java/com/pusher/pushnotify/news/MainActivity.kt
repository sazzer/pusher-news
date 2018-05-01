package com.pusher.pushnotify.news

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ListView
import com.google.firebase.messaging.RemoteMessage
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.pusher.pushnotifications.PushNotificationReceivedListener
import com.pusher.pushnotifications.PushNotifications
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var recordAdapter: SectionEntryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        PushNotifications.start(getApplicationContext(), "PUSHER_INSTANCE_ID")

        recordAdapter = SectionEntryAdapter(this)
        val recordsView = findViewById<View>(R.id.records_view) as ListView
        recordsView.setAdapter(recordAdapter)

        refreshEventsList()

        receiveNotifications()

    }

    private fun receiveNotifications() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("news",
                    "Pusher News",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        var notificationId = 0

        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, object : PushNotificationReceivedListener {
            override fun onMessageReceived(remoteMessage: RemoteMessage) {
                Log.v("ReceivedMessage", remoteMessage.data.toString())
                val headine = remoteMessage.data["headline"]
                val url = remoteMessage.data["url"]
                val trailText = remoteMessage.data["trailText"]
                val thumbnail = remoteMessage.data["thumbnail"]

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)


                val notification = NotificationCompat.Builder(applicationContext, "news")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(headine)
                        .setContentText(trailText)
                        .setLargeIcon(thumbnail?.let { getBitmapfromUrl(it) })
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)

                notificationManager.notify(notificationId++, notification.build())

            }
        });
    }

    fun getBitmapfromUrl(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            null
        }
    }

    private fun refreshEventsList() {
        val client = AsyncHttpClient()
        val subscriptions = PushNotifications.getSubscriptions()
        Log.v("Subscriptions", subscriptions.toString())

        client.get("http://10.0.2.2:8080/sections", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, response: JSONArray) {
                super.onSuccess(statusCode, headers, response)
                runOnUiThread {
                    val events = IntRange(0, response.length() - 1)
                            .map { index -> response.getJSONObject(index) }
                            .map { obj ->
                                val id = obj.getString("id")
                                SectionEntry(
                                        id = id,
                                        webTitle = obj.getString("webTitle"),
                                        subscribed = subscriptions.contains(id)
                                )
                            }

                    recordAdapter.records = events
                }
            }
        })
    }

}
