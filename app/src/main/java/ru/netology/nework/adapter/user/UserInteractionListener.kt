package ru.netology.nework.adapter.user

import ru.netology.nework.model.UserResponse

interface UserInteractionListener {
    fun onUser(user: UserResponse) {}
    fun onCheck(userId: Long){}
}