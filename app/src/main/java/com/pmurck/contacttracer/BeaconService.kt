
package com.pmurck.contacttracer

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.mohsenoid.closetome.CloseToMe
import com.mohsenoid.closetome.CloseToMeCallback
import com.mohsenoid.closetome.CloseToMeState
import com.pmurck.contacttracer.database.PingDAO
import com.pmurck.contacttracer.database.AppDatabase
import com.pmurck.contacttracer.model.Ping
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

class BeaconService : LifecycleService() {



    private lateinit var closeToMe: CloseToMe

    companion object {
        const val CHANNEL_ID: String = "beacon_notif_channel"
        private val _state = MutableLiveData<CloseToMeState>(CloseToMeState.STOPPED)
        val state: LiveData<CloseToMeState>
            get() = _state
    }

    private val manufacturerUuid = UUID.fromString("01234567-89AB-CD01-2345-67890ABCD012")

    private val dni: Int = Random.nextInt(1000000,99999999)

    private var userUuid = UUID.randomUUID()

    private lateinit var pingDAO: PingDAO

    override fun onCreate() {
        super.onCreate()

        pingDAO = AppDatabase.getInstance(application).pingDAO

        userUuid = UUID.fromString(UUID.randomUUID().toString().replaceBefore("-", dni.toString().padStart(8, '0')))

        initCloseToMe()

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }



        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Beacon Runnning")
            //.setContentText(getText(R.string.notification_message))
            .setSmallIcon(R.drawable.icon_beacon_running_notification)
            .setContentIntent(pendingIntent)
            //.setTicker(getText(R.string.ticker_text))
            .build()

// Notification ID cannot be 0.
        val ONGOING_NOTIFICATION_ID = 2
        startForeground(ONGOING_NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("iniciando")
        super.onStartCommand(intent, flags, startId)

        if (closeToMe?.isBluetoothEnabled?.value != true) {
            log("Enabling bluetooth...")

            closeToMe?.enableBluetooth(object : CloseToMeCallback {
                override fun onSuccess() {
                    log("Bluetooth is on now")
                    //startCloseToMe()
                }

                override fun onError(throwable: Throwable) {
                    log(throwable.message ?: throwable.toString())
                }
            })
        } else {
            //startCloseToMe()
            closeToMe?.start(object : CloseToMeCallback {
                override fun onSuccess() {
                    log("Beacon started successfully!")
                }

                override fun onError(throwable: Throwable) {
                    log(throwable.message ?: throwable.toString())
                }
            })
        }

        return START_STICKY
    }

    override fun onDestroy() {
        log("Destruyendo")
        super.onDestroy()

        closeToMe.stop(object : CloseToMeCallback {
            override fun onSuccess() {
                log("Beacon stopped successfully!")
            }

            override fun onError(throwable: Throwable) {
                log(throwable.message ?: throwable.toString())
            }
        })

        _state.value = CloseToMeState.STOPPED
    }

    @ExperimentalUnsignedTypes
    private fun initCloseToMe() {
        closeToMe = CloseToMe.Builder(this, manufacturerUuid)
            .setUserUuid(userUuid)
            .setMajor(1U)
            .setMinor(1U)
            .setVisibilityDistanceMeter(3.0)
            .setVisibilityTimeoutMs(5_000)
            .build().also {

                if (!it.hasBleFeature()) {
                    Toast.makeText(this, "Este dispositivo no soporta Bluetooth LE", Toast.LENGTH_LONG).show()
                    stopSelf() //finish() // stopSelf()
                }

                it.state.observe(this, Observer { state ->
                    log("Beacon state: $state")
                    _state.value = state
                })

                it.results.observe(this, Observer { beacons ->

                    beacons.values.forEach { beacon ->
                        lifecycleScope.launch {
                            pingDAO.insertIgnoreExisting(Ping(dniNumber = beacon.userUuid!!.substringBefore('-').toInt(), unixTimeStamp = beacon.lastSeen, distance = beacon.distanceInMeter))
                        }
                    }

                })
            }
    }

    fun log(msg: String) {
        Log.i("BeaconService", msg)
    }

}
