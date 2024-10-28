package com.ikki.dicodingevent.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_entity")
data class EventEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val startTime: String,
    val imageUrl: String
)