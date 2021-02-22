package com.pmurck.contacttracer.stays

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pmurck.contacttracer.databinding.ListItemStayBinding
import com.pmurck.contacttracer.model.Stay

class StayAdapter: ListAdapter<Stay, StayAdapter.ViewHolder>(StayDiffCallback()) {

    class ViewHolder(val binding: ListItemStayBinding): RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parentViewGroup: ViewGroup): ViewHolder{

                val layoutInflater = LayoutInflater.from(parentViewGroup.context)
                val binding = ListItemStayBinding.inflate(layoutInflater, parentViewGroup, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Stay) {
            binding.stay = item
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

class StayDiffCallback: DiffUtil.ItemCallback<Stay>() {
    override fun areItemsTheSame(oldItem: Stay, newItem: Stay): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Stay, newItem: Stay): Boolean {
        return oldItem == newItem
    }

}