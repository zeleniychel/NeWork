package ru.netology.nework.adapter

import ru.netology.nework.model.Post

interface OnPostInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onPost(post: Post) {}
    fun onRemove(post: Post) {}
    fun onEdit(post: Post) {}
}