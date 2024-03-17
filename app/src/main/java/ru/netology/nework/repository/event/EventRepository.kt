package ru.netology.nework.repository.event

import ru.netology.nework.model.AttachModel
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Event
import ru.netology.nework.model.Media
import java.io.File

interface EventRepository {
    suspend fun getEvents(): List<Event>
    suspend fun likeEventById(event: Event)
    suspend fun saveEvent(event: Event)
    suspend fun saveMedia(file: File): Media
    suspend fun upload(attachment: AttachModel): Media
    suspend fun saveEventWithAttachment(event: Event, media: Media, type: AttachmentType)
    suspend fun removeEventById(id:Long)
}