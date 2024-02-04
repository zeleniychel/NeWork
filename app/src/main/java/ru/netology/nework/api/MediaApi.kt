package ru.netology.nework.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.netology.nework.model.Media

interface MediaApi {

    @Multipart
    @POST("media")
    suspend fun saveMedia(@Part part: MultipartBody.Part): Response<Media>
}