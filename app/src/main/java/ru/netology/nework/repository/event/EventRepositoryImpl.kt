package ru.netology.nework.repository.event

import androidx.paging.Pager
import androidx.paging.PagingConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import ru.netology.nework.api.EventsApi
import ru.netology.nework.api.MediaApi
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import ru.netology.nework.model.Attachment
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Event
import ru.netology.nework.model.Media
import ru.netology.nework.model.PhotoModel
import java.io.File
import java.io.IOException
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val api: EventsApi,
    private val mediaApi: MediaApi
) : EventRepository {

    override val data = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = {
            EventPagingSource(api)
        }
    ).flow
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
    private suspend fun saveMedia(file: File): Response<Media> {
        val part = MultipartBody.Part.createFormData("file", file.name, file.asRequestBody())
        return mediaApi.saveMedia(part)
    }
    override suspend fun saveEvent(event: Event) {
        try {
            val response = api.saveEvent(event)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun saveEventWithAttachment(event: Event, attachment: PhotoModel) {
        try {
            val mediaResponse = saveMedia(attachment.file!!)
            if (!mediaResponse.isSuccessful) {
                throw ApiError(mediaResponse.code(), mediaResponse.message())
            }
            val media = mediaResponse.body() ?: throw ApiError(
                mediaResponse.code(),
                mediaResponse.message()
            )
            val response = api.saveEvent(event.copy(
                attachment = Attachment(
                    media.url,
                    AttachmentType.IMAGE
                )
            ))
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun removeEventById(id: Long) {
        try {
            val response = api.removeEventById(id)
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
