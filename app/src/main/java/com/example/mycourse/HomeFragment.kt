package com.example.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mycourse.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnMateriOop: Button = view.findViewById(R.id.btnMateriOop)
        val btnMateriAndroid: Button = view.findViewById(R.id.btnMateriAndroid)

        btnMateriOop.setOnClickListener {
            Toast.makeText(context, "Materi OOP clicked", Toast.LENGTH_SHORT).show()
        }

        btnMateriAndroid.setOnClickListener {
            Toast.makeText(context, "Materi Android clicked", Toast.LENGTH_SHORT).show()
        }
    }
}