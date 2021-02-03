package com.pmurck.contacttracer

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pmurck.contacttracer.database.AppDatabase
import com.pmurck.contacttracer.model.Contact
import com.pmurck.contacttracer.model.Stay
import com.pmurck.contacttracer.network.BackendApi
import com.pmurck.contacttracer.network.BluetoothContact
import com.pmurck.contacttracer.network.QrCodeGeneratedStay
import com.pmurck.contacttracer.network.User
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class BackendSynchronizationWorker(appContext: Context, workerParams: WorkerParameters):
        CoroutineWorker(appContext, workerParams) {

    companion object { // TODO: mover a otro lugar / esconder
        private const val USERNAME = "user@lala.com"
        private const val PASSWORD = "user@lala.com"
    }

    private fun milliDiffToSeconds(startMilli: Long, endMilli: Long): Long =
        TimeUnit.SECONDS.convert(endMilli - startMilli, TimeUnit.MILLISECONDS)

    private fun dateFormattedFromTimestamp(timestamp: Long): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(timestamp)).toString()

    private fun btContactFrom(contact: Contact): BluetoothContact =
        BluetoothContact(
            myDeviceId = myDni.toString(),
            otherDeviceId = contact.dniNumber.toString(),
            distanceInMeters = contact.avg_distance,
            timeInSeconds = milliDiffToSeconds(contact.startTimestamp, contact.endTimestamp),
            date = dateFormattedFromTimestamp(contact.startTimestamp)
        )

    private fun qrStayContactFrom(stay: Stay): QrCodeGeneratedStay =
        QrCodeGeneratedStay(
            myDeviceId = myDni.toString(),
            timeInSeconds = milliDiffToSeconds(stay.startTimestamp, stay.endTimestamp!!), //no deberia tomar las estadias en curso
            date = dateFormattedFromTimestamp(stay.startTimestamp),
            qrCode = stay.qrCode
        )

    private var myDni by Delegates.notNull<Int>()

    override suspend fun doWork(): Result {
        val apiService = BackendApi.retrofitService
        // on timeout, que falle
        val loginResponse = apiService.login(User(USERNAME, PASSWORD))
        if (!loginResponse.isSuccessful) {
            Log.i(
                "BackendSyncWorker",
                "Fallo la autenticación contra el backend ${BackendApi.BASE_URL} - Error: ${loginResponse.code()}"
            )
            return Result.failure()
        }

        val authToken = loginResponse.body()!!.code

        val sharedPrefs = applicationContext.getSharedPreferences(Constants.SHARED_PREFS_CONFIG_NAME, Context.MODE_PRIVATE)
        myDni = sharedPrefs.getInt(Constants.DEVICE_ID_PREF_KEY, 99_999_999) //no deberia usar defValue
        val db = AppDatabase.getInstance(applicationContext)
        val stayDao = db.stayDAO
        val contactDao = db.contactDAO

        // upload Contacts
        val contactsToUpload = contactDao.getNotUploaded()
        for (contact in contactsToUpload) {
            val response = apiService.createBluetoothContact(authToken, btContactFrom(contact))
            if (response.isSuccessful) {
                contact.uploadedContactId = response.body()!!.contactId
                contactDao.update(contact)
            } else {
                Log.i(
                    "BackendSyncWorker",
                    "Fallo la carga de contacto contra el backend ${BackendApi.BASE_URL} - Error: ${response.code()}\nJSON: ${btContactFrom(contact)}"
                )
                // Result.failure() ? pensar
            }
        }

        // upload Stays
        val staysToUpload = stayDao.getNotUploaded()
        for (stay in staysToUpload) {
            val response = apiService.createStayWithQrCode(authToken, qrStayContactFrom(stay))
            if (response.isSuccessful) {
                stay.uploadedContactId = response.body()!!.contactId
                stayDao.update(stay)
            } else {
                Log.i(
                    "BackendSyncWorker",
                    "Fallo la carga de estadia contra el backend ${BackendApi.BASE_URL} - Error: ${response.code()}\nJSON: ${qrStayContactFrom(stay)}"
                )
                // Result.failure() ? pensar
            }
        }

        return Result.success()
    }
}