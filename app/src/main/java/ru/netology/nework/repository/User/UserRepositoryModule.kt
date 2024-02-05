package ru.netology.nework.repository.User

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface UserRepositoryModule {
    @Singleton
    @Binds
    fun bindsUserRepository(impl: UserRepositoryImpl) : UserRepository
}