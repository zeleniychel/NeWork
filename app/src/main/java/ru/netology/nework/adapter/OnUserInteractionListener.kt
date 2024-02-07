package ru.netology.nework.adapter

import ru.netology.nework.model.UserPreview
import ru.netology.nework.model.UserResponse

interface OnUserInteractionListener {

    fun onUser(user:UserResponse){}
}