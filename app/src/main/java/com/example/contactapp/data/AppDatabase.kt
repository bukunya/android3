package com.example.contactapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.also
import kotlin.jvm.java

// app database
// digunakan untuk source of database dari aplikasi

@Database(entities = [Contact::class], version = 1, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        // get instance dari database
        fun get(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                name = "contacts.db"
            ).build().also { INSTANCE = it }
        }
    }

}