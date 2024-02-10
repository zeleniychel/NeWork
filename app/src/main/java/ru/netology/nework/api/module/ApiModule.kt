package ru.netology.nework.api.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.netology.nework.BuildConfig
import ru.netology.nework.api.CommentsApi
import ru.netology.nework.api.EventsApi
import ru.netology.nework.api.JobsApi
import ru.netology.nework.api.MediaApi
import ru.netology.nework.api.MyJobApi
import ru.netology.nework.api.MyWallApi
import ru.netology.nework.api.PostsApi
import ru.netology.nework.api.UsersApi
import ru.netology.nework.api.WallApi
import ru.netology.nework.auth.AppAuth
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    companion object {
        private const val BASE_URL = "${BuildConfig.BASE_URL}/api/"
    }

    @Singleton
    @Provides
    fun provideLogging() = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideOkHttp (
        logging:HttpLoggingInterceptor,
        appAuth: AppAuth
    ) : OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequestBuilder = chain.request().newBuilder()
            appAuth.authStateFlow.value.token?.let { token ->
                newRequestBuilder.addHeader("Authorization", token)
            }
            newRequestBuilder.addHeader("Api-Key", API_KEY)
            val newRequest = newRequestBuilder.build()
            return@addInterceptor chain.proceed(newRequest)
        }
        .addInterceptor(logging)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideCommentsApi(
        retrofit:Retrofit
    ): CommentsApi = retrofit.create()

    @Singleton
    @Provides
    fun provideEventsApi(
        retrofit:Retrofit
    ): EventsApi = retrofit.create()

    @Singleton
    @Provides
    fun provideJobsApi(
        retrofit:Retrofit
    ): JobsApi = retrofit.create()

    @Singleton
    @Provides
    fun provideMediaApi(
        retrofit:Retrofit
    ): MediaApi = retrofit.create()

    @Singleton
    @Provides
    fun provideMyJobApi(
        retrofit:Retrofit
    ): MyJobApi = retrofit.create()

    @Singleton
    @Provides
    fun provideMyWallApi(
        retrofit:Retrofit
    ): MyWallApi = retrofit.create()

    @Singleton
    @Provides
    fun providePostsApi(
        retrofit:Retrofit
    ): PostsApi = retrofit.create()

    @Singleton
    @Provides
    fun provideUsersApi(
        retrofit:Retrofit
    ): UsersApi = retrofit.create()

    @Singleton
    @Provides
    fun provideWallApi(
        retrofit:Retrofit
    ): WallApi = retrofit.create()
}