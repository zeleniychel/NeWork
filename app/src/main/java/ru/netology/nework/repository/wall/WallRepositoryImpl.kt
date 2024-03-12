package ru.netology.nework.repository.wall

import androidx.paging.Pager
import androidx.paging.PagingConfig
import ru.netology.nework.api.JobsApi
import ru.netology.nework.api.WallApi
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import ru.netology.nework.model.Job
import ru.netology.nework.model.Post
import java.io.IOException
import javax.inject.Inject

class WallRepositoryImpl @Inject constructor(
    private val apiWall: WallApi,
    private val apiJob: JobsApi
) : WallRepository {

    override val data = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = {
           WallPagingSource(apiWall)
        }
    ).flow
    override suspend fun getWall(authorId:Long): List<Post> {
        try {
            val response = apiWall.getWall(authorId)
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

    override suspend fun getJobs(userId: Long): List<Job> {
        try {
            val response = apiJob.getJobsByUserId(userId)
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
                apiWall.unlikePostById(post.authorId,post.id)
            } else {
                apiWall.likePostById(post.authorId,post.id)
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