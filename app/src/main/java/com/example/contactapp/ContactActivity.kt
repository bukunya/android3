package com.example.contactapp

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ContactActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var adapter: ContactAdapter
    private lateinit var contactList: MutableList<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        prefManager = PrefManager.getInstance(this)
        contactList = prefManager.getAllContacts()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewContacts)
        val btnTambah = findViewById<Button>(R.id.btnTambahKontak)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        adapter = ContactAdapter(this, contactList, prefManager)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnTambah.setOnClickListener {
            showAddContactDialog()
        }

        btnKembali.setOnClickListener {
            finish()
        }
    }

    private fun showAddContactDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_contact, null)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPhone = view.findViewById<EditText>(R.id.etPhone)

        AlertDialog.Builder(this)
            .setTitle("Tambah Kontak")
            .setView(view)
            .setPositiveButton("Simpan") { _, _ ->
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val phone = etPhone.text.toString()

                if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
                    prefManager.saveContact(name, email, phone)
                    contactList = prefManager.getAllContacts()
                    adapter = ContactAdapter(this, contactList, prefManager)
                    findViewById<RecyclerView>(R.id.recyclerViewContacts).adapter = adapter
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
