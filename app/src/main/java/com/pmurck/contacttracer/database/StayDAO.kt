package com.pmurck.contacttracer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pmurck.contacttracer.model.Stay

@Dao
interface StayDAO {

    @Insert
    suspend fun insert(stay: Stay)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     */
    @Update
    suspend fun update(stay: Stay)

    @Query("SELECT * from stay WHERE id = :key")
    suspend fun get(key: Long): Stay?

    @Query("SELECT * from stay WHERE timestamp_end IS NULL")
    fun getCurrent(): LiveData<Stay?>

    // Sin la posible estadia en curso
    @Query("SELECT * FROM stay WHERE timestamp_end IS NOT NULL AND uploaded_contact_id IS NULL")
    suspend fun getNotUploaded(): List<Stay>

}