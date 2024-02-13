package ru.netology.nework.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPreview(
    val name: String,
    val avatar: String?
) : Parcelable
