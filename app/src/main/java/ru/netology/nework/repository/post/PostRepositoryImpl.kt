package ru.netology.nework.repository.post

import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import ru.netology.nework.api.MediaApi
import ru.netology.nework.api.PostsApi
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import ru.netology.nework.model.Attachment
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Media
import ru.netology.nework.model.PhotoModel
import ru.netology.nework.model.Post
import java.io.File
import java.io.IOException
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val api: PostsApi,
    private val mediaApi: MediaApi
) : PostRepository {
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

    private suspend fun saveMedia(file: File): Response<Media> {
        val part = MultipartBody.Part.createFormData("file", file.name, file.asRequestBody())
        return mediaApi.saveMedia(part)
    }

    override suspend fun savePostWithAttachment(post: Post, attachment: PhotoModel) {
        try {
            val mediaResponse = saveMedia(attachment.file!!)
            if (!mediaResponse.isSuccessful) {
                throw ApiError(mediaResponse.code(), mediaResponse.message())
            }
            val media = mediaResponse.body() ?: throw ApiError(
                mediaResponse.code(),
                mediaResponse.message()
            )
            val response = api.savePost(post.copy(
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
}