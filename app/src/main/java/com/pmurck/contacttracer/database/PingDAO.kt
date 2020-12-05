
package com.pmurck.contacttracer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pmurck.contacttracer.model.Ping

@Dao
interface PingDAO {

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
    suspend fun get(key: Long): Ping


    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM bt_beacon_ping ORDER BY id DESC")
    fun getAll(): LiveData<List<Ping>>

}
