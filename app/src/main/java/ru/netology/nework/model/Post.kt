package ru.netology.nework.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: Long = 0,
    val authorId: Long = 0,
    val author: String ="",
    val authorJob: String? = null,
    val authorAvatar: String? = null,
    val content: String="",
    val published: String="",
    val coords: Coordinates? = null,
    val link: String? = null,
    val mentionIds: List<Long> = listOf(),
    val mentionedMe: Boolean = false,
    val likeOwnerIds: List<Long> = listOf(),
    val likeByMe: Boolean = false,
    val attachment: Attachment? = null,
    val users: Map<String,UserPreview> = mapOf(),
):Parcelable