package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ru.netology.nework.model.Job

interface JobsApi {
    @GET("{userId}/jobs")
    suspend fun getJobsByUserId(@Path("userId")userId:Long): Response<List<Job>>
}