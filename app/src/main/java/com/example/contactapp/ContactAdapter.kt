package com.example.contactapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(
    private val context: Context,
    private var contactList: MutableList<Contact>,
    private val prefManager: PrefManager
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btn_edit_contact)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete_contact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = contactList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactList[position]

        holder.tvName.text = contact.name
        holder.tvEmail.text = contact.email
        holder.tvPhone.text = contact.phone

        // Tombol Edit
        holder.btnEdit.setOnClickListener {
            showEditDialog(position, contact)
        }

        // Tombol Hapus
        holder.btnDelete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Hapus Kontak")
                .setMessage("Yakin ingin menghapus kontak ini?")
                .setPositiveButton("Ya") { _, _ ->
                    prefManager.deleteContact(position)
                    contactList = prefManager.getAllContacts()
                    notifyDataSetChanged()
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun showEditDialog(position: Int, contact: Contact) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_edit_contact, null)
        val nameField = view.findViewById<TextView>(R.id.etName)
        val emailField = view.findViewById<TextView>(R.id.etEmail)
        val phoneField = view.findViewById<TextView>(R.id.etPhone)

        nameField.text = contact.name
        emailField.text = contact.email
        phoneField.text = contact.phone

        AlertDialog.Builder(context)
            .setTitle("Edit Kontak")
            .setView(view)
            .setPositiveButton("Simpan") { _, _ ->
                val updated = Contact(
                    nameField.text.toString(),
                    emailField.text.toString(),
                    phoneField.text.toString()
                )
                prefManager.editContact(position, updated.name, updated.email, updated.phone)
                contactList = prefManager.getAllContacts()
                notifyDataSetChanged()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
