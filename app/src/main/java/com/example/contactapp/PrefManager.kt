package com.example.contactapp

import android.content.Context
import android.content.SharedPreferences

class PrefManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences
    companion object {
        private const val PREFS_FILENAME = "AuthAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"

        private const val KEY_CONTACT_COUNT = "contact_count" // jumlah kontak
        private const val KEY_CONTACT_NAME_PREFIX = "contact_name_"
        private const val KEY_CONTACT_EMAIL_PREFIX = "contact_email_"
        private const val KEY_CONTACT_PHONE_PREFIX = "contact_phone_"


        @Volatile
        private var instance: PrefManager? = null
        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }

            }
        }
    }
    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILENAME,
            Context.MODE_PRIVATE)
    }
    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }
    fun savePassword(password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }
    fun getUsername(): String {
        return sharedPreferences.getString(KEY_USERNAME, "") ?: ""
    }
    fun getPassword(): String {
        return sharedPreferences.getString(KEY_PASSWORD, "") ?: ""
    }
    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun saveContact(name: String, email: String, phone: String) {
        val count = sharedPreferences.getInt(KEY_CONTACT_COUNT, 0) + 1
        val editor = sharedPreferences.edit()

        editor.putString(KEY_CONTACT_NAME_PREFIX + count, name)
        editor.putString(KEY_CONTACT_EMAIL_PREFIX + count, email)
        editor.putString(KEY_CONTACT_PHONE_PREFIX + count, phone)
        editor.putInt(KEY_CONTACT_COUNT, count)

        editor.apply()
    }

    fun getAllContacts(): MutableList<Contact> {
        val list = mutableListOf<Contact>()
        val count = sharedPreferences.getInt(KEY_CONTACT_COUNT, 0)

        for (i in 1..count) {
            val name = sharedPreferences.getString(KEY_CONTACT_NAME_PREFIX + i, "-") ?: "-"
            val email = sharedPreferences.getString(KEY_CONTACT_EMAIL_PREFIX + i, "-") ?: "-"
            val phone = sharedPreferences.getString(KEY_CONTACT_PHONE_PREFIX + i, "-") ?: "-"
            list.add(Contact(name, email, phone))
        }

        return list
    }

    fun editContact(position: Int, name: String, email: String, phone: String) {
        val editor = sharedPreferences.edit()
        val index = position + 1 // karena kita mulai dari 1
        editor.putString(KEY_CONTACT_NAME_PREFIX + index, name)
        editor.putString(KEY_CONTACT_EMAIL_PREFIX + index, email)
        editor.putString(KEY_CONTACT_PHONE_PREFIX + index, phone)
        editor.apply()
    }

    fun deleteContact(position: Int) {
        val count = sharedPreferences.getInt(KEY_CONTACT_COUNT, 0)
        val editor = sharedPreferences.edit()
        val index = position + 1

        // Hapus data di posisi yang dipilih
        editor.remove(KEY_CONTACT_NAME_PREFIX + index)
        editor.remove(KEY_CONTACT_EMAIL_PREFIX + index)
        editor.remove(KEY_CONTACT_PHONE_PREFIX + index)

        // Geser data setelahnya agar tetap urut
        for (i in index until count) {
            val nextName = sharedPreferences.getString(KEY_CONTACT_NAME_PREFIX + (i + 1), null)
            val nextEmail = sharedPreferences.getString(KEY_CONTACT_EMAIL_PREFIX + (i + 1), null)
            val nextPhone = sharedPreferences.getString(KEY_CONTACT_PHONE_PREFIX + (i + 1), null)

            editor.putString(KEY_CONTACT_NAME_PREFIX + i, nextName)
            editor.putString(KEY_CONTACT_EMAIL_PREFIX + i, nextEmail)
            editor.putString(KEY_CONTACT_PHONE_PREFIX + i, nextPhone)
        }

        // Kurangi jumlah total kontak
        editor.putInt(KEY_CONTACT_COUNT, count - 1)
        editor.apply()
    }

}