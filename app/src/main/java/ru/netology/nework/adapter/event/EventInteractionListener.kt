package ru.netology.nework.adapter.event

import ru.netology.nework.model.Event

interface EventInteractionListener {
    fun onEvent(event: Event) {}
    fun onLike(event: Event) {}
    fun onMedia(event: Event) {}
    fun onEdit(event: Event) {}
    fun onRemove(event: Event) {}
}