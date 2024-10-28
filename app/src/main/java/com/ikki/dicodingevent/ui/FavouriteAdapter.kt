package com.ikki.dicodingevent.ui

import android.content.Intent
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikki.dicodingevent.DetailEvent
import com.ikki.dicodingevent.R
import com.ikki.dicodingevent.data.entity.EventEntity
import com.ikki.dicodingevent.databinding.ItemEventFavouriteBinding

class FavouriteAdapter : ListAdapter<EventEntity, FavouriteAdapter.FavouriteViewHolder>(DIFF_CALLBACK) {

    class FavouriteViewHolder(private val binding: ItemEventFavouriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity) {
            binding.apply {
                textEventName.text = Html.fromHtml(event.name, Html.FROM_HTML_MODE_COMPACT)
                val descriptionPreview = if (event.description.length > 100) {
                    "${event.description.substring(0, 100)}..."
                } else {
                    event.description
                }
                textSummary.text = Html.fromHtml(descriptionPreview, Html.FROM_HTML_MODE_LEGACY)
                textStartDate.text = event.startTime

                Glide.with(itemView.context)
                    .load(event.imageUrl)
                    .into(imageEvent)
            }
            binding.root.setOnClickListener{
                val intent = Intent(binding.root.context, DetailEvent::class.java). apply{
                    putExtra("id", event.id)
                }
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val binding = ItemEventFavouriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    override fun submitList(list: List<EventEntity>?) {
        super.submitList(list ?: emptyList())
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}