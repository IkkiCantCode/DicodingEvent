package com.ikki.dicodingevent.data

import androidx.lifecycle.LiveData
import com.ikki.dicodingevent.data.entity.EventEntity
import com.ikki.dicodingevent.data.room.EventDao

class EventRepository(private val eventDao: EventDao) {

    suspend fun bookmarkEvent(eventId: Int, name: String, description: String, startTime: String, imageUrl: String) {
        val eventEntity = EventEntity(
            id = eventId,
            name = name,
            description = description,
            startTime = startTime,
            imageUrl = imageUrl
        )
        eventDao.insertBookmarkedEvent(eventEntity)
    }

    suspend fun unbookmarkEvent(eventId: Int) {
        val eventEntity = eventDao.getBookmarkedEventSync(eventId)
        eventEntity?.let {
            eventDao.deleteBookmarkedEvent(it)
        }
    }

    fun getAllBookmarkedEvents(): LiveData<List<EventEntity>> {
        return eventDao.getAllBookmarkedEvents()
    }

    fun getBookmarkedEvent(eventId: Int): LiveData<EventEntity?> {
        return eventDao.getBookmarkedEventById(eventId)
    }
}