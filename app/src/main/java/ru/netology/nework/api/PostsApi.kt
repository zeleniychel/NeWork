package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.nework.model.Post

interface PostsApi {
    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>

    @POST("posts")
    suspend fun savePost(@Body post: Post): Response<Post>

    @POST("posts/{id}/likes")
    suspend fun likePostById(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun unlikePostById(@Path("id") id: Long): Response<Post>

    @GET("posts/{id}/newer")
    suspend fun getNewerPosts(@Path("id") id: Long): Response<List<Post>>

    @GET("posts/{id}/before")
    suspend fun getPostsBefore(@Path("id") id: Long, @Query("count") count: Int): Response<List<Post>>

    @GET("posts/{id}/after")
    suspend fun getPostsAfter(@Path("id") id: Long, @Query("count") count: Int): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id:Long): Response<Post>

    @DELETE("posts/{id}")
    suspend fun removePostById(@Path("id") id: Long): Response<Unit>

    @GET("posts/latest")
    suspend fun getLatestPosts(@Query("count") count: Int): Response<List<Post>>
}