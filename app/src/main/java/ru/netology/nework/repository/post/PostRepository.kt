package ru.netology.nework.repository.post

import ru.netology.nework.model.Post

interface PostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun likePostById(post: Post)
    suspend fun savePost(post: Post)
}