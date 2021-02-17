package com.pmurck.contacttracer.pings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pmurck.contacttracer.databinding.ListItemPingBinding
import com.pmurck.contacttracer.model.Ping

class PingAdapter: ListAdapter<Ping, PingAdapter.ViewHolder>(PingDiffCallback()) {

    class ViewHolder(val binding: ListItemPingBinding): RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parentViewGroup: ViewGroup): ViewHolder{

                val layoutInflater = LayoutInflater.from(parentViewGroup.context)
                val binding = ListItemPingBinding.inflate(layoutInflater, parentViewGroup, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Ping) {
            binding.ping = item
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

class PingDiffCallback: DiffUtil.ItemCallback<Ping>() {
    override fun areItemsTheSame(oldItem: Ping, newItem: Ping): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Ping, newItem: Ping): Boolean {
        return oldItem == newItem
    }

}