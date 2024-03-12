package ru.netology.nework.repository.post

import androidx.paging.Pager
import androidx.paging.PagingConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nework.api.MediaApi
import ru.netology.nework.api.PostsApi
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import ru.netology.nework.model.Attachment
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Media
import ru.netology.nework.model.Post
import ru.netology.nework.viewmodel.AttachModel
import java.io.File
import java.io.IOException
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val api: PostsApi,
    private val mediaApi: MediaApi
) : PostRepository {
    override val data = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = {
            PostPagingSource(api)
        }
    ).flow

    override suspend fun getPosts(): List<Post> {
        try {
            val response = api.getPosts()
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

    override suspend fun likePostById(post: Post) {
        try {
            val response = if (post.likeByMe) {
                api.unlikePostById(post.id)
            } else {
                api.likePostById(post.id)
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

    override suspend fun removePostById(id: Long) {
        try {
            val response = api.removePostById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun savePost(post: Post) {
        try {
            val response = api.savePost(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun saveMedia(file: File): Media {
        val part = MultipartBody.Part.createFormData("file", file.name, file.asRequestBody())
        val response = mediaApi.saveMedia(part)
        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        }
        val body = response.body() ?: throw ApiError(
            response.code(),
            response.message()
        )
        return body
    }

    override suspend fun upload(attachment: AttachModel): Media {
        val emptyRequestBody: RequestBody = "".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        var part: MultipartBody.Part = MultipartBody.Part.createFormData("empty_part", "", emptyRequestBody)
        if (attachment.inputStream != null) {
            when (attachment.type) {
                AttachmentType.IMAGE -> part = MultipartBody.Part.createFormData(
                    "image",
                    "myImage",
                    attachment.inputStream.readBytes().toRequestBody("image/*".toMediaType())
                )

                AttachmentType.AUDIO -> part = MultipartBody.Part.createFormData(
                    "audio",
                    "myAudio",
                    attachment.inputStream.readBytes().toRequestBody("audio/*".toMediaType())
                )

                AttachmentType.VIDEO -> part = MultipartBody.Part.createFormData(
                    "video",
                    "myVideo",
                    attachment.inputStream.readBytes().toRequestBody("video/*".toMediaType())
                )

                else -> {}
            }
        }

        val response = mediaApi.saveMedia(part)
        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        }
        val body = response.body() ?: throw ApiError(
            response.code(),
            response.message()
        )
        return body
    }

    override suspend fun savePostWithAttachment(post: Post, attachment: Media) {
        try {
            val response = api.savePost(
                post.copy(
                    attachment = Attachment(
                        attachment.url,
                        AttachmentType.AUDIO
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
}