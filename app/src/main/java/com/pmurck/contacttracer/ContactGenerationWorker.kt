package com.pmurck.contacttracer

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pmurck.contacttracer.database.AppDatabase
import com.pmurck.contacttracer.model.Contact
import com.pmurck.contacttracer.model.Ping
import java.util.*

class ContactGenerationWorker(appContext: Context, workerParams: WorkerParameters):
        CoroutineWorker(appContext, workerParams) {

    /*
        firstPing.timestamp <= secondPing.timestamp
     */
    private fun calcDistanceSumBySeconds(firstPing: Ping, secondPing: Ping): Double {
        // interpolacion lineal de distancia en el tiempo
        val seconds = (secondPing.unixTimeStamp - firstPing.unixTimeStamp) / 1000
        val avg_distance = (secondPing.distance + firstPing.distance) / 2
        return seconds * avg_distance
    }

    override suspend fun doWork(): Result {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val lastContactGenerationTimestamp = sharedPrefs.getLong(Constants.CONTACT_GEN_TIMESTAMP_PREF_KEY, 0L)
        val currentContactGenerationTimestamp = System.currentTimeMillis()
        Log.d("ContactGenWorker", "Iniciando el worker, tomamos pings desde ${Date(lastContactGenerationTimestamp)}")

        val MIN_CONTACT_TIME_IN_SECONDS = sharedPrefs.getInt(Constants.CONTACT_GEN_MIN_TIME_IN_MINUTES_PREF_KEY, 15) * 60
        val MAX_TIME_DIFF_BETWEEN_PINGS_FOR_CONTACT_IN_SECONDS = sharedPrefs.getInt(Constants.CONTACT_GEN_MAX_TIME_DIFF_BETWEEN_PINGS_IN_SECONDS_PREF_KEY, 60).toLong()

        Log.d("ContactGenWorker", "Requisitos de gen: duracion minima = ${MIN_CONTACT_TIME_IN_SECONDS}s ; diff maxima entre pings = ${MAX_TIME_DIFF_BETWEEN_PINGS_FOR_CONTACT_IN_SECONDS}s")
        val db = AppDatabase.getInstance(applicationContext)
        val pingDao = db.pingDAO
        val contactDao = db.contactDAO

                                                                // para tomar algunos pings previos del corte
        val pinged_dnis = pingDao.getUniqueDNIsSince(lastContactGenerationTimestamp - (MIN_CONTACT_TIME_IN_SECONDS * 1000 / 2))

        for (dni in pinged_dnis) {
            Log.d("ContactGenWorker", "Calculando contactos para el DNI: $dni")
            val pings = pingDao.getFromDniSince(dni, lastContactGenerationTimestamp - (MIN_CONTACT_TIME_IN_SECONDS * 1000 / 2))

            var distanceSumBySeconds = 0.0
            var firstPing = pings.first()
            var lastPing = firstPing

            // TODO: pensar sacar x% con las distancias mas altas y bajas
            //  -> implicaria cambiar la distancia sumada por lista o rango de lista original con pings del contacto
            for (currentPing in pings) {
                // Si es el ultimo, chequearlo
                if (currentPing === pings.last() ||
                    currentPing.unixTimeStamp - lastPing.unixTimeStamp > MAX_TIME_DIFF_BETWEEN_PINGS_FOR_CONTACT_IN_SECONDS*1000) {
                    // posible Contacto entre firstPing hasta lastPing
                    val contactLengthInSeconds = (lastPing.unixTimeStamp - firstPing.unixTimeStamp) / 1000
                    if (contactLengthInSeconds >= MIN_CONTACT_TIME_IN_SECONDS){
                        val avg_distance = distanceSumBySeconds / (contactLengthInSeconds)

                        contactDao.insertIgnoreExisting(Contact(
                                dniNumber = firstPing.dniNumber,
                                startTimestamp = firstPing.unixTimeStamp,
                                endTimestamp = lastPing.unixTimeStamp,
                                avg_distance = avg_distance))
                    }
                    firstPing = currentPing
                    distanceSumBySeconds = 0.0

                } else {
                    //sumar al calculo de distancia
                    distanceSumBySeconds += calcDistanceSumBySeconds(lastPing, currentPing)
                }
                lastPing = currentPing
            }
        }

        //actualizar las preferences para la proxima ejecución
        sharedPrefs.edit {
            putLong(Constants.CONTACT_GEN_TIMESTAMP_PREF_KEY, currentContactGenerationTimestamp)
        }

        return Result.success()
    }
}