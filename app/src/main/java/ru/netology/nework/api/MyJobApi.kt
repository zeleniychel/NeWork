package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nework.model.Job

interface MyJobApi {

    @GET("my/jobs")
    suspend fun getMyJob(): Response<List<Job>>

    @POST("my/jobs")
    suspend fun saveMyJob(@Body job: Job): Response<Job>

    @DELETE ("my/jobs/{id}")
    suspend fun deleteMyJob(@Path("id") id: Long):Response<Unit>
}