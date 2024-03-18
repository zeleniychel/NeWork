package ru.netology.nework.repository.post

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.model.AttachModel
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Media
import ru.netology.nework.model.Post
import java.io.File

interface PostRepository {
    val data: Flow<PagingData<Post>>
    suspend fun getPosts(): List<Post>
    suspend fun likePostById(post: Post)
    suspend fun saveMedia(file: File): Media
    suspend fun upload(attachment: AttachModel): Media
    suspend fun savePost(post: Post)
    suspend fun savePostWithAttachment(post: Post, media: Media, type: AttachmentType)
    suspend fun removePostById(id:Long)
}