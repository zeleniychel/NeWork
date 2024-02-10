package ru.netology.nework.repository.User

import ru.netology.nework.model.Token
import ru.netology.nework.model.UserResponse
import java.io.File

interface UserRepository {
    suspend fun getUsers(): List<UserResponse>
    suspend fun getUserById(id: Long): UserResponse
    suspend fun registerUser(login: String, pass: String, name: String, avatar: File?): Token
    suspend fun updateUser(login: String, pass: String): Token
}