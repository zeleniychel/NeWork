package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.nework.model.Post

interface WallApi {

    @POST("{authorId}/wall/{id}/likes")
    suspend fun likePostById(@Path("authorId") authorId: Long, @Path("id") id: Long): Response<Post>

    @DELETE("{authorId}/wall/{id}/likes")
    suspend fun unlikePostById(@Path("authorId") authorId: Long, @Path("id") id: Long): Response<Post>

    @GET("{authorId}/wall")
    suspend fun getWall (@Path("authorId") authorId: Long): Response<List<Post>>

    @GET("{authorId}/wall/{id}/newer")
    suspend fun getNewerWallPosts (@Path("authorId") authorId: Long, @Path("id") id: Long): Response<List<Post>>

    @GET("{authorId}/wall/{id}/before")
    suspend fun getWallPostsBefore (@Path("authorId") authorId: Long, @Path("id") id: Long, @Query("count") count: Int): Response<List<Post>>

    @GET("{authorId}/wall/{id}/after")
    suspend fun getWallPostsAfter (@Path("authorId") authorId: Long, @Path("id") id: Long, @Query("count") count: Int): Response<List<Post>>

    @GET("{authorId}/wall/{id}")
    suspend fun getWallPostById (@Path("authorId") authorId: Long, @Path("id") id: Long): Response<Post>

    @GET("{authorId}/wall/latest")
    suspend fun getLatestWallPosts (@Query("count") count: Int): Response<List<Post>>
}