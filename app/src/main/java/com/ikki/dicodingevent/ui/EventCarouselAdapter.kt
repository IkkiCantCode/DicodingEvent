package com.ikki.dicodingevent.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikki.dicodingevent.DetailEvent
import com.ikki.dicodingevent.data.response.ListEventsItem
import com.ikki.dicodingevent.databinding.ItemCarouselEventBinding

class EventCarouselAdapter(private val events: List<ListEventsItem>) : RecyclerView.Adapter<EventCarouselAdapter.EventViewHolder>() {

    class EventViewHolder(private val binding: ItemCarouselEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.textEventTitle.text = event.name
            Glide.with(binding.root.context)
                .load(event.imageLogo)
                .into(binding.imageEvent)

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailEvent::class.java).apply {
                    putExtra("id", event.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemCarouselEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = events.size
}