package com.ismealdi.hidoc.utils.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ismealdi.hidoc.App
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.struct.Location
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Logs
import com.ismealdi.hidoc.utils.commons.Preferences
import com.ismealdi.hidoc.utils.extensions.location
import com.ismealdi.hidoc.utils.extensions.user
import com.ismealdi.hidoc.view.user.UserMainActivity


/**
 * Created by Al on 16/10/2018
 */

class AmMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Logs.fireBase("From: ${remoteMessage?.from}")

        remoteMessage?.data?.isNotEmpty()?.run {
            sendNotification(remoteMessage.data["description"] ?:  "", remoteMessage.data["title"] ?:  "", (0..500).random())
        }

    }

    override fun onNewToken(token: String?) {
        Logs.fireBase("Refreshed token: " + token!!)
        storeToken(token)
        Preferences(this).storeToken(token)
    }

    fun storeLocation(location: GeoPoint) {
        App.amAuth.currentUser?.let{ user ->
            val data : MutableMap<String, Any> = mutableMapOf()

            data[User.CREATOR.Fields.location] = location

            App.amDatabase.user(user.uid).update(data.toMap()).addOnSuccessListener {
                Logs.fireBase("Last location, ${location.latitude} | ${location.longitude}")
            }.addOnFailureListener {
                Logs.fireBase("Failed update state: " + it.message)
            }

            App.amDatabase.location().add(Location(location = location, uid = user.uid))
        }
    }

    fun storeOnline(state: Boolean) {
        App.amAuth.currentUser?.let{ user ->
            val data : MutableMap<String, Any> = mutableMapOf()

            data[User.CREATOR.Fields.onlineUser] = state
            data[User.CREATOR.Fields.lastUpdated] = Timestamp.now()

            App.amDatabase.user(user.uid).update(data.toMap()).addOnSuccessListener {
                Logs.fireBase("State changed to $state")
            }.addOnFailureListener {
                Logs.fireBase("Failed update state: " + it.message)
            }
        }
    }

    fun storeToken(token: String) {
        if(token.isNotEmpty()){

            val data : MutableMap<String, Any> = mutableMapOf()

            data[User.CREATOR.Fields.pushId] = token
            data[User.CREATOR.Fields.lastUpdated] = Timestamp.now()

            App.amAuth.currentUser?.let{ user ->
                App.amDatabase.user(user.uid).update(data.toMap())
                    .addOnFailureListener {
                        Logs.fireBase("Failed update token: " + it.message)
                    }
            }
        }
    }

    private fun sendNotification(body: String, title: String, from: Int) {
        val intent = Intent(this, UserMainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        //intent.putExtra(Constants.INTENT.NOTIFICATION, response)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.app_channel)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(from, notificationBuilder.build())
    }


}