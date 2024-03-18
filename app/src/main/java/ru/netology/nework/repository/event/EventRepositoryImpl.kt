package ru.netology.nework.repository.event

import androidx.paging.Pager
import androidx.paging.PagingConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nework.api.EventsApi
import ru.netology.nework.api.MediaApi
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import ru.netology.nework.model.AttachModel
import ru.netology.nework.model.Attachment
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Event
import ru.netology.nework.model.Media
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
    override suspend fun saveMedia(file: File): Media {
        val part = MultipartBody.Part.createFormData("file", file.name, file.asRequestBody())
        val response = mediaApi.saveMedia(part)
        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        }
        return response.body() ?: throw ApiError(
            response.code(),
            response.message()
        )
    }

    override suspend fun upload(attachment: AttachModel): Media {
        val emptyRequestBody: RequestBody =
            "".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        var part: MultipartBody.Part =
            MultipartBody.Part.createFormData("empty_part", "", emptyRequestBody)
        val requestBody = attachment.bytes?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        if (attachment.bytes != null) {
            when (attachment.type) {
                AttachmentType.IMAGE -> part = MultipartBody.Part.createFormData(
                    "file",
                    "myImage.jpg",
                    requestBody!!
                )

                AttachmentType.AUDIO -> part = MultipartBody.Part.createFormData(
                    "file",
                    "myAudio.mp3",
                    attachment.bytes.toRequestBody("audio/*".toMediaType())
                )

                AttachmentType.VIDEO -> part = MultipartBody.Part.createFormData(
                    "file",
                    "myVideo.mp4",
                    attachment.bytes.toRequestBody("video/*".toMediaType())
                )

                else -> {}
            }
        }

        val response = mediaApi.saveMedia(part)
        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        }
        return response.body() ?: throw ApiError(
            response.code(),
            response.message()
        )
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

    override suspend fun saveEventWithAttachment(event: Event, media: Media, type: AttachmentType ) {
        try {
            val response = api.saveEvent(
                event.copy(
                    attachment = Attachment(
                        media.url,
                        type
                    )
                )
            )
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
