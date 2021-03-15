package com.pmurck.contacttracer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set default prefs
        PreferenceManager.setDefaultValues(applicationContext, R.xml.root_preferences, true)

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

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        setupContactGenWorkerKeepExisting(applicationContext, sharedPrefs.getInt(Constants.CONTACT_GEN_REPEAT_INTERVAL_HOURS_PREF_KEY, 8))
        setupBackendSyncWorkerKeepExisting(applicationContext, sharedPrefs.getInt(Constants.BACKEND_SYNC_REPEAT_INTERVAL_HOURS_PREF_KEY, 8))


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

}