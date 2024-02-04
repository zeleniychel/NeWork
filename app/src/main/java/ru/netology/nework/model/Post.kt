package ru.netology.nework.model

data class Post(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorJob: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val coords: Coordinates,
    val link: String,
    val mentionIds: List<Long>,
    val mentionedMe: Boolean,
    val likeOwnerIds: List<Long>,
    val likeByMe: Boolean,
    val attachment: Attachment,
    val users: UserPreview
)