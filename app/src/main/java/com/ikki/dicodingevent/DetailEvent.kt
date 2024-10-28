package com.ikki.dicodingevent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.ikki.dicodingevent.data.DetailViewModelFactory
import com.ikki.dicodingevent.data.di.Injection
import com.ikki.dicodingevent.databinding.ActivityDetailEventBinding
import com.ikki.dicodingevent.ui.DetailViewModel

class DetailEvent : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = Injection.provideRepository(this)
        val factory = DetailViewModelFactory(repository)
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        val eventId = intent.getIntExtra("id", -1)
        detailViewModel.setID(eventId)

        binding.progressBar.visibility = View.VISIBLE

        detailViewModel.eventDetail.observe(this) { eventDetail ->
            binding.progressBar.visibility = View.GONE
            eventDetail?.event?.let { event ->
                bindEventData(
                    event.name,
                    event.ownerName,
                    event.description,
                    event.cityName,
                    event.beginTime,
                    event.endTime,
                    event.quota,
                    event.registrants,
                    event.mediaCover,
                    event.link
                )
                binding.buttonLink.setOnClickListener {
                    openLink(event.link)
                }
                detailViewModel.isBookmarked(eventId).observe(this) { isBookmarked ->
                    updateBookmarkIcon(isBookmarked)

                    binding.favBookmark.setOnClickListener {
                        val newBookmarkState = !isBookmarked
                        if (newBookmarkState) {
                            detailViewModel.bookmarkEvent(
                                eventId,
                                event.name,
                                event.description,
                                event.beginTime,
                                event.imageLogo
                            )
                        } else {
                            detailViewModel.unbookmarkEvent(eventId)
                        }
                        updateBookmarkIcon(newBookmarkState)
                    }
                }
            }
        }

        detailViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
                detailViewModel.clearErrorMessage()
            }
        }
    }

    private fun bindEventData(
        eventName: String,
        ownerName: String,
        description: String,
        cityName: String,
        beginTime: String,
        endTime: String,
        quota: Int,
        registrants: Int,
        mediaCover: String,
        link: String
    ) {
        val remainingQuota = quota - registrants
        binding.apply {
            textEventTitle.text = eventName
            textOwnerName.text = getString(R.string.owner_name_format, ownerName)
            textSummary.text = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
            textLocation.text = cityName
            textStartDate.text = getString(R.string.start_date_format, beginTime)
            textEndDate.text = getString(R.string.end_date_format, endTime)
            textQuota.text = getString(R.string.remaining_quota_format, remainingQuota)

            Glide.with(this@DetailEvent)
                .load(mediaCover)
                .into(imageMediaCover)
        }
    }

    private fun openLink(url: String) {
        if (url.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun updateBookmarkIcon(bookmarked: Boolean) {
        if (bookmarked) {
            binding.favBookmark.setImageResource(R.drawable.ic_bookmark_check)
        } else {
            binding.favBookmark.setImageResource(R.drawable.ic_bookmark_add)
        }
    }
}