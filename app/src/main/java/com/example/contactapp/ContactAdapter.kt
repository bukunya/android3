package com.example.contactapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.Contact
import com.example.contactapp.databinding.ItemContactBinding

typealias onClickContact = (Contact) -> Unit

class ContactAdapter(private val listContact: List<Contact>, private val onClickContact: onClickContact):
    RecyclerView.Adapter<ContactAdapter.ItemContactViewHolder>() {
    inner class ItemContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Contact){
            with(binding){
                tvContactName.text = data.name
                tvContactEmail.text = data.email
                tvContactPhoneNumber.text = data.phone
                itemView.setOnClickListener {
                    onClickContact(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int):
            ItemContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ItemContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemContactViewHolder, position: Int) {
        holder.bind(listContact[position])
    }

    override fun getItemCount(): Int {
        return listContact.count()
    }
}