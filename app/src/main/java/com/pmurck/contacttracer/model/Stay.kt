package com.pmurck.contacttracer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "stay", indices = arrayOf(Index(value = ["qrCode","timestamp_start"], unique = true)))
data class Stay(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,

        @ColumnInfo(name = "qrCode")
        val qrCode: String,

        @ColumnInfo(name = "timestamp_start")
        val startTimestamp: Long,

        @ColumnInfo(name = "timestamp_end")
        val endTimestamp: Long? = null,
)