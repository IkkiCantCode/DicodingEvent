package com.ikki.dicodingevent.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikki.dicodingevent.data.response.ListEventsItem
import com.ikki.dicodingevent.databinding.ItemCarouselEventBinding

class CarouselEventAdapter(private val events: List<ListEventsItem>) :
    RecyclerView.Adapter<CarouselEventAdapter.CarouselViewHolder>() {

    class CarouselViewHolder(private val binding: ItemCarouselEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.textEventTitle.text = event.name
            Glide.with(binding.imageEvent.context)
                .load(event.imageLogo)
                .into(binding.imageEvent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ItemCarouselEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}