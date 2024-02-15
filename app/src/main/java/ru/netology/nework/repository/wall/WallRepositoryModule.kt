package ru.netology.nework.repository.wall

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface WallRepositoryModule {
    @Singleton
    @Binds
    fun bindsPostRepository(impl: WallRepositoryImpl) : WallRepository
}