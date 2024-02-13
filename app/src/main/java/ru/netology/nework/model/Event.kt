package ru.netology.nework.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val id: Long= 0,
    val authorId: Long = 0,
    val author: String ="",
    val authorJob: String? =null,
    val authorAvatar: String? = null,
    val content: String = "",
    val datetime: String = "",
    val published: String = "",
    val coords: Coordinates? = null,
    val type: EventType = EventType.ONLINE,
    val likeOwnerIds: List<Long> = listOf(),
    val likeByMe: Boolean = false,
    val speakerIds: List<Long> = listOf(),
    val participantsIds: List<Long> = listOf(),
    val participantsByMe: Boolean = false,
    val attachment: Attachment? = null,
    val link: String? = null,
    val users: Map<String,UserPreview> = mapOf(),
):Parcelable
