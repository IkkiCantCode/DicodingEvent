package com.ikki.dicodingevent.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikki.dicodingevent.data.response.ListEventsItem
import com.ikki.dicodingevent.data.retrofit.ApiService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationsViewModel : ViewModel() {

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val apiService = ApiService.create()

    init {
        fetchFinishedEvents()
    }

    private fun fetchFinishedEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getAllEvents()
                val finishedEvents = response.listEvents.filter { isEventFinished(it.endTime) }
                if (finishedEvents.isNotEmpty()) {
                    _finishedEvents.postValue(finishedEvents)
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "No finished events found."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load events: ${e.message}"
                _finishedEvents.postValue(emptyList())
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun isEventFinished(endTime: String): Boolean {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return try {
            val eventEndDate = inputFormat.parse(endTime)
            eventEndDate?.before(Date()) == true
        } catch (e: Exception) {
            false
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}