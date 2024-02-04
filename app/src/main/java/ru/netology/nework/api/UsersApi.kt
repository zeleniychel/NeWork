package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

import ru.netology.nework.model.Token
import ru.netology.nework.model.UserResponse

interface UsersApi {

    @GET("users")
    suspend fun getUsers(): Response<List<UserResponse>>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id")id:Long): Response<UserResponse>

    @FormUrlEncoded
    @POST("users/registration")
    suspend fun registerUser(
        @Field("login") login: String,
        @Field("pass") pass: String,
        @Field("name") name: String
    ): Response<Token>

    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun updateUser(
        @Field("login") login: String,
        @Field("pass") pass: String
    ): Response<Token>


}