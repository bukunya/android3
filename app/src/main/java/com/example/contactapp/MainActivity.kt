package com.example.contactapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    private lateinit var adapter: ContactAdapter
    private lateinit var contactList: MutableList<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(0, statusBarInsets.top, 0, 0)
            insets
        }

        setSupportActionBar(binding.toolbar)

        prefManager = PrefManager.getInstance(this)
        contactList = prefManager.getAllContacts()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewContacts)

        adapter = ContactAdapter(this, contactList, prefManager)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        checkLoginStatus()

        with(binding) {
            recyclerViewContacts.apply {
                layoutManager = GridLayoutManager(this@MainActivity, 1)
            }

            btnTambahKontak.setOnClickListener {
                showAddContactDialog()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (!isLoggedIn) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
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
                    val contactList = prefManager.getAllContacts()
                    val adapter = ContactAdapter(this, contactList, prefManager)
                    findViewById<RecyclerView>(R.id.recyclerViewContacts).adapter = adapter
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
