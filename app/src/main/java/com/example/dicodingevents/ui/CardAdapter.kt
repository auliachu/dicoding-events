package com.example.dicodingevents.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevents.data.response.DicodingEventsResponse
import com.example.dicodingevents.data.response.ListEventsItem
import com.example.dicodingevents.databinding.ItemEventBinding

class CardAdapter(private val onClick: (String) -> Unit) : ListAdapter<ListEventsItem, CardAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardAdapter.MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

    }

    class MyViewHolder(private val binding: ItemEventBinding, private val onClick: (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
           binding.apply {
               Glide.with(root.context)
                   .load(event.mediaCover)
                   .into(imgItemPhoto)
               eventTitle.text = event.name
               val eventId = event.id.toString()
               root.setOnClickListener{
                   onClick(eventId)
               }
           }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}