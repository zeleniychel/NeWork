package ru.netology.nework.repository.event

import ru.netology.nework.model.Event
import ru.netology.nework.model.PhotoModel

interface EventRepository {
    suspend fun getEvents(): List<Event>
    suspend fun likeEventById(event: Event)
    suspend fun saveEvent(event: Event)
    suspend fun saveEventWithAttachment(event: Event, attachment: PhotoModel)
    suspend fun removeEventById(id:Long)
}