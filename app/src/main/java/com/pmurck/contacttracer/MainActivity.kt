package com.pmurck.contacttracer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (notificationManager.getNotificationChannel(BeaconService.CHANNEL_ID) == null) {
                val name = "Notificaciones del Beacon"
                val importance = NotificationManager.IMPORTANCE_LOW
                val channel = NotificationChannel(BeaconService.CHANNEL_ID, name, importance)
                // Register the channel with the system

                notificationManager.createNotificationChannel(channel)
            }
        }
    }

}