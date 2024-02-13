package ru.netology.nework.adapter.post

import ru.netology.nework.model.Post

interface PostInteractionListener {
    fun onPost(post: Post) {}
    fun onLike(post: Post) {}
    fun onMedia(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
}