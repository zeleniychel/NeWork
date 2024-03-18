package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.nework.model.Post

interface MyWallApi {

    @POST("my/wall/{id}/likes")
    suspend fun likeMyPostById(@Path("id") id: Long): Response<Post>

    @DELETE("my/wall/{id}/likes")
    suspend fun unlikeMyPostById(@Path("id") id: Long): Response<Post>

    @GET("my/wall")
    suspend fun getMyWall(): Response<List<Post>>

    @GET("my/wall/{id}/newer")
    suspend fun getNewerMyWallPosts(@Path("id") id: Long): Response<List<Post>>

    @GET("my/wall/{id}/before")
    suspend fun getMyWallPostsBefore(@Path("id") id: Long,@Query("count") count: Int): Response<List<Post>>

    @GET("my/wall/{id}/after")
    suspend fun getMyWallPostsAfter(@Path("id") id: Long,@Query("count") count: Int): Response<List<Post>>

    @GET("my/wall/{id}")
    suspend fun getMyWallPostById(@Path("id") id: Long): Response<Post>

    @GET("my/wall/latest")
    suspend fun getLatestMyWallPosts (@Query("count") count: Int): Response<List<Post>>
}