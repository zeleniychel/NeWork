package ru.netology.nework.repository.mywall

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface MyWallRepositoryModule {
    @Singleton
    @Binds
    fun bindsPostRepository(impl: MyWallRepositoryImpl) : MyWallRepository
}