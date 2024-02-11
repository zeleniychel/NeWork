package ru.netology.nework.model

data class UserResponse(
    override val id: Long,
    val login: String,
    val name: String,
    val avatar: String
): FeedItem
