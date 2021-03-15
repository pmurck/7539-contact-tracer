package com.pmurck.contacttracer

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun dateFormatter(date: Date): String {
    return SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date).toString()
}

fun dateFormatter(timestamp: Long): String = dateFormatter(Date(timestamp))

fun setupContactGenWorker(context: Context, repeatIntervalInHours: Int, workPolicy: ExistingPeriodicWorkPolicy) {
    val contactGenWorkRequest = PeriodicWorkRequestBuilder<ContactGenerationWorker>(repeatIntervalInHours.toLong(), TimeUnit.HOURS)
        .build()
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "contactGeneration",
        workPolicy,
        contactGenWorkRequest
    )
}

fun setupContactGenWorkerReplaceExisting(context: Context, repeatIntervalInHours: Int) {
    setupContactGenWorker(context, repeatIntervalInHours, ExistingPeriodicWorkPolicy.REPLACE)
}

fun setupContactGenWorkerKeepExisting(context: Context, repeatIntervalInHours: Int) {
    setupContactGenWorker(context, repeatIntervalInHours, ExistingPeriodicWorkPolicy.KEEP)
}

fun setupBackendSyncWorker(context: Context, repeatIntervalInHours: Int, workPolicy: ExistingPeriodicWorkPolicy) {
    val backendSyncWorkRequest = PeriodicWorkRequestBuilder<BackendSynchronizationWorker>(repeatIntervalInHours.toLong(), TimeUnit.HOURS)
        .build()
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "backendSynchronization",
        workPolicy,
        backendSyncWorkRequest
    )
}

fun setupBackendSyncWorkerReplaceExisting(context: Context, repeatIntervalInHours: Int) {
    setupBackendSyncWorker(context, repeatIntervalInHours, ExistingPeriodicWorkPolicy.REPLACE)
}

fun setupBackendSyncWorkerKeepExisting(context: Context, repeatIntervalInHours: Int) {
    setupBackendSyncWorker(context, repeatIntervalInHours, ExistingPeriodicWorkPolicy.KEEP)
}