package com.ikki.dicodingevent.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ikki.dicodingevent.ui.DetailViewModel

class DetailViewModelFactory(private val repository: EventRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(repository) as T
    }
}