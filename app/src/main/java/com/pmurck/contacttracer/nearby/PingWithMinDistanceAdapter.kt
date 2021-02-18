package com.pmurck.contacttracer.nearby

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pmurck.contacttracer.database.PingDAO
import com.pmurck.contacttracer.databinding.ListItemPingBinding
import com.pmurck.contacttracer.databinding.ListItemPingWithMinDistanceBinding
import com.pmurck.contacttracer.model.Ping

class PingWithMinDistanceAdapter:
    ListAdapter<PingDAO.PingWithMinDistance, PingWithMinDistanceAdapter.ViewHolder>(PingWithMinDistanceDiffCallback()) {

    class ViewHolder(val binding: ListItemPingWithMinDistanceBinding): RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parentViewGroup: ViewGroup): ViewHolder{

                val layoutInflater = LayoutInflater.from(parentViewGroup.context)
                val binding = ListItemPingWithMinDistanceBinding.inflate(layoutInflater, parentViewGroup, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: PingDAO.PingWithMinDistance) {
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

class PingWithMinDistanceDiffCallback: DiffUtil.ItemCallback<PingDAO.PingWithMinDistance>() {
    override fun areItemsTheSame(oldItem: PingDAO.PingWithMinDistance, newItem: PingDAO.PingWithMinDistance): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PingDAO.PingWithMinDistance, newItem: PingDAO.PingWithMinDistance): Boolean {
        return oldItem == newItem
    }

}