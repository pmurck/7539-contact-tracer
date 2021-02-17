package com.pmurck.contacttracer

import java.text.SimpleDateFormat
import java.util.*


fun dateFormatter(date: Date): String {
    return SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date).toString()
}

fun dateFormatter(timestamp: Long): String = dateFormatter(Date(timestamp))