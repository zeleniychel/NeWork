package ru.netology.nework.repository.User

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.model.Token
import ru.netology.nework.model.UserResponse
import java.io.File

interface UserRepository {
    suspend fun getUsers(): Flow<List<UserResponse>>
    suspend fun getUserById(id: Long): UserResponse
    suspend fun registerUserWithAvatar(login: String, pass: String, name: String, avatar: File): Token
    suspend fun registerUser(login: String, pass: String, name: String): Token
    suspend fun updateUser(login: String, pass: String): Token
}