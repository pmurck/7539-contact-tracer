
package com.pmurck.contacttracer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pmurck.contacttracer.model.Ping

@Dao
interface PingDAO {

    // TODO: insert vararg
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnoreExisting(ping: Ping)

    @Insert
    suspend fun insert(ping: Ping)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     */
    @Update
    suspend fun update(ping: Ping)

    @Query("SELECT * from bt_beacon_ping WHERE id = :key")
    suspend fun get(key: Long): Ping?


    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM bt_beacon_ping ORDER BY id DESC")
    fun getAll(): LiveData<List<Ping>>

    @Query("SELECT p.*, l.minDistance FROM bt_beacon_ping p NATURAL JOIN (SELECT MAX(id) as id, MIN(distance) as minDistance FROM bt_beacon_ping WHERE unix_time_stamp >= strftime('%s', 'now','-'||:minutes||' minutes')*1000 GROUP BY dni_number) l")
    fun getLastFromDistinctDNISinceMinutesBeforeNow(minutes: Int): LiveData<List<PingWithMinDistance>>

    @Query("SELECT DISTINCT dni_number FROM bt_beacon_ping WHERE unix_time_stamp >= :timestamp")
    suspend fun getUniqueDNIsSince(timestamp: Long): List<Int>

    @Query("SELECT * FROM bt_beacon_ping WHERE dni_number == :dni AND unix_time_stamp >= :timestamp ORDER BY unix_time_stamp ASC")
    suspend fun getFromDniSince(dni: Int, timestamp: Long): List<Ping>

    data class PingWithMinDistance(
        val id: Long,

        @ColumnInfo(name = "dni_number")
        val dniNumber: Int,

        @ColumnInfo(name = "unix_time_stamp")
        val unixTimeStamp: Long,

        @ColumnInfo(name = "distance")
        val distance: Double,

        val minDistance: Double
    )
}
