package com.pmurck.contacttracer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "bt_beacon_ping", indices = arrayOf(Index(value = ["dni_number","unix_time_stamp"], unique = true)))
data class Ping(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "dni_number")
    val dniNumber: Int,

    @ColumnInfo(name = "unix_time_stamp")
    val unixTimeStamp: Long,

    @ColumnInfo(name = "distance")
    val distance: Double
)