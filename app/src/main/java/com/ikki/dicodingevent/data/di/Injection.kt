package com.ikki.dicodingevent.data.di

import android.content.Context
import com.ikki.dicodingevent.data.EventRepository
import com.ikki.dicodingevent.data.room.EventDatabase

object Injection {

    fun provideRepository(context: Context): EventRepository {
        val database = EventDatabase.getDatabase(context)
        val eventDao = database.eventDao()
        return EventRepository(eventDao)
    }
}