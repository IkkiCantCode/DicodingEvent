package com.ikki.dicodingevent.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikki.dicodingevent.data.response.ListEventsItem
import com.ikki.dicodingevent.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DashboardViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> get() = _upcomingEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    init {
        fetchUpcomingEvents()
    }

    private fun fetchUpcomingEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val apiService = ApiService.create()
                val response = withContext(Dispatchers.IO) {
                    apiService.getUpcomingEvents()
                }
                if (response.listEvents.isNotEmpty()) {
                    _upcomingEvents.value = response.listEvents
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "No events found."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load events: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}