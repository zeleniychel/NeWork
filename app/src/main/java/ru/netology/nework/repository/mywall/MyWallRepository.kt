package ru.netology.nework.repository.mywall

import ru.netology.nework.model.Job
import ru.netology.nework.model.Post

interface MyWallRepository {
    suspend fun getMyWall(): List<Post>
    suspend fun likeMyPostById(post: Post)
    suspend fun getMyJobs():List<Job>
    suspend fun saveMyJob(job: Job)
    suspend fun deleteMyJob(id: Long)
}