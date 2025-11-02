package com.example.contactapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.data.Contact
import com.example.contactapp.databinding.ItemContactBinding

class ContactAdapter(
    private val onEdit: (Contact) -> Unit,
    private val onDelete: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ItemContactViewHolder>() {

    private val contacts = mutableListOf<Contact>()

    inner class ItemContactViewHolder(private val binding: ItemContactBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun binding(contact: Contact) {
            with(binding) {
                tvContactName.text = contact.name
                tvContactEmail.text = contact.email
                tvContactPhoneNumber.text = contact.phone

                btnEditContact.setOnClickListener { onEdit(contact) }
                btnDeleteContact.setOnClickListener { onDelete(contact) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemContactViewHolder {
        val binding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemContactViewHolder(binding)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ItemContactViewHolder, position: Int) {
        holder.binding(contacts[position])
    }

    fun setItems(newData: List<Contact>) {
        contacts.clear()
        contacts.addAll(newData)
        notifyDataSetChanged()
    }
}