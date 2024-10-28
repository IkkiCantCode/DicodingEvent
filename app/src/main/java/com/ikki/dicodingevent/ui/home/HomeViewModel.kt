package com.ikki.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikki.dicodingevent.data.response.ListEventsItem
import com.ikki.dicodingevent.data.retrofit.ApiService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HomeViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val apiService: ApiService = ApiService.create()

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllEvents()
                val currentDateMillis = System.currentTimeMillis()
                val upcoming = mutableListOf<ListEventsItem>()
                val finished = mutableListOf<ListEventsItem>()

                response.listEvents.forEach { event ->
                    val eventEndTimeMillis = event.endTime.toMillis()
                    if (eventEndTimeMillis > currentDateMillis) {
                        upcoming.add(event)
                    } else {
                        finished.add(event)
                    }
                }
                _upcomingEvents.postValue(upcoming)
                _finishedEvents.postValue(finished)
                _errorMessage.postValue(null)
            } catch (e: Exception) {
                _upcomingEvents.postValue(emptyList())
                _finishedEvents.postValue(emptyList())
                _errorMessage.postValue("Failed to get event, check your connection")
            }
        }
    }

    private fun String.toMillis(): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.parse(this)?.time ?: 0L
    }

}