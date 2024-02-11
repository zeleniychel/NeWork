package ru.netology.nework.repository.post

import ru.netology.nework.api.PostsApi
import ru.netology.nework.api.UsersApi
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import ru.netology.nework.model.Post
import java.io.IOException
import javax.inject.Inject

class PostRepositoryImpl@Inject constructor(
    private val api: PostsApi
): PostRepository {
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
}