package com.example.contactapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// data

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,   // autogenerate, fied name= id, type Long integer
    val name: String,
    val email: String,
    val phone: String
)