package com.example.contactapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.contactapp.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity() {
    lateinit var binding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityContactBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //panggil fungsi generate dummydata
        var disaster = generateDummy()

        //init adapter
        val adapterContact = ContactAdapter(generateDummy()) { contact ->
            Toast.makeText(
                this@ContactActivity,
                "You clicked on ${contact.name}",
                Toast.LENGTH_SHORT
            ).show()
        }

        with(binding){
            rvContact.apply {
                adapter = adapterContact
                layoutManager = GridLayoutManager(this@ContactActivity, 1)
            }
            btnReturnToMain.setOnClickListener {
                startActivity(Intent(this@ContactActivity, MainActivity::class.java))
                finish()
            }

        }
    }

    fun generateDummy(): List<Contact>{
        return listOf(
            Contact(name = "Supri", "supri@mail.com", "1234567890"),
            Contact(name = "Yanto", "yanto@mail.com", "0987654321"),
            Contact(name = "Gunawan", "gunawan@mail.com", "1133557799"),
            Contact(name = "Sunardi", "sunardi@mail.com", "2244668800"),
            Contact(name = "Eko", "eko@mail.com", "1324576890"),
            Contact(name = "Mulyono", "jokowi@mail.com", "6769420000")
        )
    }
}