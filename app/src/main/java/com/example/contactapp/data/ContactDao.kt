package com.example.contactapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

//Data Access Object
// sebuah interface, untuk berkomunikasi dengan data contact

@Dao
interface ContactDao {
    @Insert
    suspend fun insert(contact: Contact): Long

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    suspend fun getAll(): List<Contact>
}