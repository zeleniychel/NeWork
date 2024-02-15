package ru.netology.nework.repository.wall

import ru.netology.nework.model.Job
import ru.netology.nework.model.Post

interface WallRepository {
    suspend fun getWall(authorId: Long): List<Post>
    suspend fun likePostById(post: Post)
    suspend fun getJobs(userId:Long):List<Job>
}