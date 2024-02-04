package ru.netology.nework.model

data class Event(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorJob: String,
    val authorAvatar: String,
    val content: String,
    val datetime: String,
    val published: String,
    val coords: Coordinates,
    val type: EventType,
    val likeOwnerIds: List<Long>,
    val likeByMe: Boolean,
    val speakerIds: List<Long>,
    val participantsIds: List<Long>,
    val participantsByMe: Boolean,
    val attachment: Attachment,
    val link: String,
    val users: UserPreview
)
