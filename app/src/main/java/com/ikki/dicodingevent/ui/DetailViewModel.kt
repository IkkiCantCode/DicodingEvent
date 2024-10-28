package com.ikki.dicodingevent.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.ikki.dicodingevent.data.EventRepository
import com.ikki.dicodingevent.data.entity.EventEntity
import com.ikki.dicodingevent.data.response.DetailEventResponse
import com.ikki.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repository: EventRepository) : ViewModel() {

    private val _eventDetail = MutableLiveData<DetailEventResponse?>()
    val eventDetail: LiveData<DetailEventResponse?> = _eventDetail

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var eventId: Int = 0

    fun setID(id: Int) {
        eventId = id
        getEventDetail(eventId)
    }

    private fun getEventDetail(eventId: Int) {
        ApiConfig.getApiService().getDetail(eventId)
            .enqueue(object : Callback<DetailEventResponse> {
                override fun onResponse(
                    call: Call<DetailEventResponse>,
                    response: Response<DetailEventResponse>
                ) {
                    if (response.isSuccessful) {
                        val eventDetail = response.body()
                        if (eventDetail != null) {
                            _eventDetail.value = eventDetail
                            _errorMessage.value = null
                        }
                    } else {
                        _errorMessage.value = "Error: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                    _errorMessage.value = "Failed to load event details: ${t.message}"
                }
            })
    }

    fun bookmarkEvent(eventId: Int, name: String, description: String, startTime: String, imageUrl: String) {
        Log.d("DetailViewModel", "Bookmarking event: $eventId")
        viewModelScope.launch {
            repository.bookmarkEvent(eventId, name, description, startTime, imageUrl)
        }
    }

    fun unbookmarkEvent(eventId: Int) {
        viewModelScope.launch {
            repository.unbookmarkEvent(eventId)
        }
    }

    fun isBookmarked(eventId: Int): LiveData<Boolean> {
        return repository.getBookmarkedEvent(eventId).map { it != null }
    }

    fun getAllBookmarkedEvents(): LiveData<List<EventEntity>> {
        _isLoading.value = true
        return liveData {
            emitSource(repository.getAllBookmarkedEvents().also {
                _isLoading.postValue(false)
            })
        }
    }


    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}