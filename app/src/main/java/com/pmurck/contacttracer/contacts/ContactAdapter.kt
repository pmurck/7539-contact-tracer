package com.pmurck.contacttracer.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pmurck.contacttracer.databinding.ListItemContactBinding
import com.pmurck.contacttracer.model.Contact

class ContactAdapter: ListAdapter<Contact, ContactAdapter.ViewHolder>(ContactDiffCallback()) {

    class ViewHolder(val binding: ListItemContactBinding): RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parentViewGroup: ViewGroup): ViewHolder{

                val layoutInflater = LayoutInflater.from(parentViewGroup.context)
                val binding = ListItemContactBinding.inflate(layoutInflater, parentViewGroup, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Contact) {
            binding.contact = item
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class ContactDiffCallback: DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }

}