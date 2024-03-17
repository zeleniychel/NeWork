package ru.netology.nework.model

import android.net.Uri

data class AttachModel(
    val bytes: ByteArray? = null,
    val type: AttachmentType? = null,
    val uri: Uri? = null,
)