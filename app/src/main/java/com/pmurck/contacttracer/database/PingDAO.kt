
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

    @Query("SELECT DISTINCT dni_number FROM bt_beacon_ping WHERE unix_time_stamp >= :timestamp")
    suspend fun getUniqueDNIsSince(timestamp: Long): List<Int>

    @Query("SELECT * FROM bt_beacon_ping WHERE dni_number == :dni AND unix_time_stamp >= :timestamp ORDER BY unix_time_stamp ASC")
    suspend fun getFromDniSince(dni: Int, timestamp: Long): List<Ping>
}
