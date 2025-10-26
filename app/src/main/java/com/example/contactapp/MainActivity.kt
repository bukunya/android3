package com.example.contactapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.contactapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager

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
        checkLoginStatus()

        val adapterContact = ContactAdapter(generateDummy()) { contact ->
            Toast.makeText(
                this@MainActivity,
                "You clicked on ${contact.name}",
                Toast.LENGTH_SHORT
            ).show()
        }

        with(binding) {
            rvContact.apply {
                adapter = adapterContact
                layoutManager = GridLayoutManager(this@MainActivity, 1)
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

    private fun generateDummy(): List<Contact> {
        return listOf(
            Contact(name = "Supri", email = "supri@mail.com", phone = "1234567890"),
            Contact(name = "Yanto", email = "yanto@mail.com", phone = "0987654321"),
            Contact(name = "Gunawan", email = "gunawan@mail.com", phone = "1133557799"),
            Contact(name = "Sunardi", email = "sunardi@mail.com", phone = "2244668800"),
            Contact(name = "Eko", email = "eko@mail.com", phone = "1324576890"),
            Contact(name = "Mulyono", email = "jokowi@mail.com", phone = "6769420000")
        )
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (!isLoggedIn) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }
}
