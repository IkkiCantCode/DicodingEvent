package com.ikki.dicodingevent.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ikki.dicodingevent.data.entity.EventEntity

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmarkedEvent(event: EventEntity)

    @Delete
    suspend fun deleteBookmarkedEvent(event: EventEntity)

    @Query("SELECT * FROM event_entity WHERE id = :eventId LIMIT 1")
    fun getBookmarkedEventById(eventId: Int): LiveData<EventEntity?>

    @Query("SELECT * FROM event_entity WHERE id = :eventId LIMIT 1")
    suspend fun getBookmarkedEventSync(eventId: Int): EventEntity?

    @Query("SELECT * FROM event_entity")
    fun getAllBookmarkedEvents(): LiveData<List<EventEntity>>
}