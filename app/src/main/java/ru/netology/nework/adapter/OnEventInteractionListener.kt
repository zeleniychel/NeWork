package ru.netology.nework.adapter

import ru.netology.nework.model.Event

interface OnEventInteractionListener {
    fun onLike(event: Event) {}
    fun onShare(event: Event) {}
    fun onEvent(event: Event) {}
    fun onRemove(event: Event) {}
    fun onEdit(event: Event) {}
}