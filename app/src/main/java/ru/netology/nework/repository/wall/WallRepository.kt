package ru.netology.nework.repository.wall

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.model.Job
import ru.netology.nework.model.Post

interface WallRepository {
    val data: Flow<PagingData<Post>>
    suspend fun getWall(authorId: Long): List<Post>
    suspend fun likePostById(post: Post)
    suspend fun getJobs(userId:Long):List<Job>
}