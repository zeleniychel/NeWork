package ru.netology.nework.repository.event

import ru.netology.nework.api.EventsApi
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import ru.netology.nework.model.Event
import java.io.IOException
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val api: EventsApi
) : EventRepository {
    override suspend fun getEvents(): List<Event> {
        try {
            val response = api.getEvents()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likeEventById(event: Event) {
        try {
            val response = if (event.likeByMe) {
                api.unlikeEventById(event.id)
            } else {
                api.likeEventById(event.id)
            }
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }

    }
}