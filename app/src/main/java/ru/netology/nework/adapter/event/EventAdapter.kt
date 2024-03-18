package ru.netology.nework.adapter.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.netology.nework.databinding.CardEventBinding
import ru.netology.nework.model.Event

class EventAdapter(
    private val eventInteractionListener: EventInteractionListener,
    private val authId: Long?
) : PagingDataAdapter<Event, EventViewHolder>(EventDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = CardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, eventInteractionListener, authId)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position) ?: return
        holder.bind(event)
    }
}

class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}