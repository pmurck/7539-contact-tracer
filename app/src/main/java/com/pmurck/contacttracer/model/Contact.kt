package com.pmurck.contacttracer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "contact", indices = arrayOf(Index(value = ["dni_number","timestamp_start"], unique = true)))
data class Contact(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,

        @ColumnInfo(name = "dni_number")
        val dniNumber: Int,

        @ColumnInfo(name = "timestamp_start")
        val startTimestamp: Long,

        @ColumnInfo(name = "timestamp_end")
        val endTimestamp: Long,

        @ColumnInfo(name = "avg_distance")
        val avg_distance: Double
)