package com.pmurck.contacttracer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pmurck.contacttracer.model.Contact

@Dao
interface ContactDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnoreExisting(contact: Contact)

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

    @Query("SELECT * FROM contact WHERE uploaded_contact_id IS NULL")
    suspend fun getNotUploaded(): List<Contact>

    @Query("SELECT * from contact ORDER BY timestamp_start DESC")
    fun getAll(): LiveData<List<Contact>>
}