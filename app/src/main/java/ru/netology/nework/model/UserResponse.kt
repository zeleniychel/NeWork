package ru.netology.nework.model

data class UserResponse(
    val id: Long,
    val login: String,
    val name: String,
    val avatar: String
)
