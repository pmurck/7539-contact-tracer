package com.pmurck.contacttracer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pmurck.contacttracer.model.Contact

@Dao
interface ContactDAO {

    @Insert
    suspend fun insert(contact: Contact)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     */
    @Update
    suspend fun update(contact: Contact)

    @Query("SELECT * from contact WHERE id = :key")
    suspend fun get(key: Long): Contact?

    @Query("SELECT * from contact ORDER BY timestamp_start DESC")
    fun getAll(): LiveData<List<Contact>>
}