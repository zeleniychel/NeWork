package ru.netology.nework.repository.mywall

import androidx.paging.Pager
import androidx.paging.PagingConfig
import ru.netology.nework.api.MyJobApi
import ru.netology.nework.api.MyWallApi
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import ru.netology.nework.model.Job
import ru.netology.nework.model.Post
import java.io.IOException
import javax.inject.Inject

class MyWallRepositoryImpl @Inject constructor(
    private val apiWall: MyWallApi,
    private val apiJob: MyJobApi
) : MyWallRepository {
    override val data = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = {
            MyWallPagingSource(apiWall)
        }
    ).flow

    override suspend fun getMyWall(): List<Post> {
        try {
            val response = apiWall.getMyWall()
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

    override suspend fun getMyJobs(): List<Job> {
        try {
            val response = apiJob.getMyJob()
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

    override suspend fun saveMyJob(job: Job) {
        try {
            val response = apiJob.saveMyJob(job)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun deleteMyJob(id: Long) {
        try {
            val response = apiJob.deleteMyJob(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likeMyPostById(post: Post) {
        try {
            val response = if (post.likeByMe) {
                apiWall.unlikeMyPostById(post.id)
            } else {
                apiWall.likeMyPostById(post.id)
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