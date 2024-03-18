package ru.netology.nework.repository.mywall

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.model.Job
import ru.netology.nework.model.Post

interface MyWallRepository {
    val data: Flow<PagingData<Post>>
    suspend fun getMyWall(): List<Post>
    suspend fun likeMyPostById(post: Post)
    suspend fun getMyJobs():List<Job>
    suspend fun saveMyJob(job: Job)
    suspend fun deleteMyJob(id: Long)
}