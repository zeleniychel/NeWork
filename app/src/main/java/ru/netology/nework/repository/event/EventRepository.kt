package ru.netology.nework.repository.event

import ru.netology.nework.model.Event

interface EventRepository {
    suspend fun getEvents(): List<Event>
    suspend fun likeEventById(event: Event)
}