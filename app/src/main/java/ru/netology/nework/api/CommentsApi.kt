package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nework.model.Comment

interface CommentsApi {

    @GET("posts/{postId}/comments")
    suspend fun getCommentsByPostId(@Path("postId")postId:Long): Response<Comment>

    @POST("posts/{postId}/comments")
    suspend fun saveComment(@Path("postId")postId:Long): Response<Comment>

    @POST("posts/{postId}/comments/{id}/likes")
    suspend fun likeComment(@Path("postId") postId: Long, @Path("id") id: Long): Response<Comment>

    @DELETE("posts/{postId}/comments/{id}/likes")
    suspend fun unlikeComment(@Path("postId") postId: Long, @Path("id") id: Long): Response<Comment>

    @DELETE("posts/{postId}/comments/{id}")
    suspend fun deleteComment(@Path("postId") postId: Long, @Path("id") id: Long): Response<Unit>

}