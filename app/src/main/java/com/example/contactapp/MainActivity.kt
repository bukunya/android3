package com.example.contactapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactapp.data.AppDatabase
import com.example.contactapp.data.Contact // Make sure this is your new Contact data class
import com.example.contactapp.databinding.ActivityMainBinding
import com.example.contactapp.databinding.DialogAddEditContactBinding // <-- IMPORT THIS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    private lateinit var adapter: ContactAdapter

    // DAO
    private val contactDao by lazy { AppDatabase.get(this).contactDao() }

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

        adapter = ContactAdapter(
            onEdit = { contact ->
                showEditContactDialog(contact)
            },
            onDelete = { contact ->
                showDeleteConfirmationDialog(contact)
            }
        )

        checkLoginStatus()

        with(binding) {
            recyclerViewContacts.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = this@MainActivity.adapter // Set the adapter instance
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
        val dialogBinding = DialogAddEditContactBinding.inflate(layoutInflater)

        AlertDialog.Builder(this)
            .setTitle("Tambah Kontak")
            .setView(dialogBinding.root)
            .setPositiveButton("Simpan") { dialog, _ ->
                val name = dialogBinding.etName.text.toString().trim()
                val email = dialogBinding.etEmail.text.toString().trim()
                val phone = dialogBinding.etPhone.text.toString().trim()

                if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        contactDao.insert(Contact(name = name, email = email, phone = phone))

                        withContext(Dispatchers.Main) {
                            refreshList()
                        }
                    }
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditContactDialog(contact: Contact) {
        val dialogBinding = DialogAddEditContactBinding.inflate(layoutInflater)

        // Pre-fill the fields with the contact's data
        dialogBinding.etName.setText(contact.name)
        dialogBinding.etEmail.setText(contact.email)
        dialogBinding.etPhone.setText(contact.phone)

        AlertDialog.Builder(this)
            .setTitle("Edit Kontak")
            .setView(dialogBinding.root)
            .setPositiveButton("Simpan") { dialog, _ ->
                val name = dialogBinding.etName.text.toString().trim()
                val email = dialogBinding.etEmail.text.toString().trim()
                val phone = dialogBinding.etPhone.text.toString().trim()

                if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        contactDao.update(contact.copy(name = name, email = email, phone = phone))

                        withContext(Dispatchers.Main) {
                            refreshList()
                        }
                    }
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDeleteConfirmationDialog(contact: Contact) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Kontak")
            .setMessage("Yakin ingin menghapus ${contact.name}?")
            .setPositiveButton("Hapus") { dialog, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    contactDao.delete(contact)
                    withContext(Dispatchers.Main) {
                        refreshList()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        lifecycleScope.launch {
            val items = withContext(Dispatchers.IO) { contactDao.getAll() }
            adapter.setItems(items)
        }
    }
}