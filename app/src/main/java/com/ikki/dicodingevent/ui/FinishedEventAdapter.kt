package com.ikki.dicodingevent.ui

import android.content.Intent
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikki.dicodingevent.DetailEvent
import com.ikki.dicodingevent.data.response.ListEventsItem
import com.ikki.dicodingevent.databinding.ItemEventFinishedBinding
import java.text.SimpleDateFormat
import java.util.Locale

class FinishedEventAdapter : ListAdapter<ListEventsItem, FinishedEventAdapter.EventViewHolder>(DIFF_CALLBACK) {

    class EventViewHolder(private val binding: ItemEventFinishedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.apply {
                textEventName.text = Html.fromHtml(event.name, Html.FROM_HTML_MODE_COMPACT)
                textSummary.text = event.summary
                textEndDate.text = formatDate(event.endTime)

                Glide.with(itemView.context)
                    .load(event.imageLogo)
                    .into(imageEvent)
            }
            binding.root.setOnClickListener{
                val intent = Intent(binding.root.context, DetailEvent::class.java). apply{
                    putExtra("id", event.id)
                }
                binding.root.context.startActivity(intent)
            }
        }

        private fun formatDate(dateString: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            return if (date != null) outputFormat.format(date) else "N/A"
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventFinishedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}