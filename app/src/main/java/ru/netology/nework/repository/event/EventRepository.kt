package ru.netology.nework.repository.event

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.model.Event
import ru.netology.nework.model.PhotoModel

interface EventRepository {
    val data: Flow<PagingData<Event>>
    suspend fun getEvents(): List<Event>
    suspend fun likeEventById(event: Event)
    suspend fun saveEvent(event: Event)
    suspend fun saveEventWithAttachment(event: Event, attachment: PhotoModel)
    suspend fun removeEventById(id:Long)
}