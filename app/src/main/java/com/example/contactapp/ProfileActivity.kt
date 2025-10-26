package com.example.contactapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.contactapp.databinding.ActivityProfileBinding // Nama binding ini sekarang benar untuk layout profil

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding // Perhatikan perubahan nama binding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        with(binding) {
            txtUsername.text = "Login sebagai: ${prefManager.getUsername()}"
            btnLogout.setOnClickListener {
                prefManager.setLoggedIn(false)
                val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            btnToContact.setOnClickListener {
                finish()
            }
        }
    }
}
