package ru.netology.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.BuildConfig
import ru.netology.nework.R
import ru.netology.nework.databinding.CardEventBinding
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Event
import ru.netology.nework.util.Converter
import ru.netology.nework.util.load
import ru.netology.nework.util.loadAttachment

class EventFeedAdapter(
    private val onEventInteractionListener: OnEventInteractionListener
) : androidx.recyclerview.widget.ListAdapter<Event, EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = CardEventBinding.inflate(LayoutInflater.from(parent.context))
        return EventViewHolder(binding, onEventInteractionListener)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }
}

class EventViewHolder(
    private val binding: CardEventBinding,
    private val onInteractionListener: OnEventInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(event: Event) {
        binding.apply {
            author.text = event.author
            published.text = event.published
            content.text = event.content
            likes.isChecked = event.likeByMe
            likes.text = Converter.convertNumber(event.likeOwnerIds.size)
            participants.text = Converter.convertNumber(event.participantsIds.size)
            avatar.load("${BuildConfig.BASE_URL}/api/users/${event.authorId}/avatar")
            imageAttachment.loadAttachment("${BuildConfig.BASE_URL}/api/event/${event.id}/attachment")
            when (event.attachment.type){
                AttachmentType.IMAGE -> {
                    imageAttachment.isVisible
                }
                AttachmentType.VIDEO->{
                    group.isVisible
                }
                AttachmentType.AUDIO -> {
                    audioAttachment.isVisible
                }
            }

            menu.isVisible = event.ownedByMe
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(event)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(event)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }


            likes.setOnClickListener {
                onInteractionListener.onLike(event)
            }
            share.setOnClickListener {
                onInteractionListener.onShare(event)
            }
            root.setOnClickListener {
                onInteractionListener.onEvent(event)
            }
        }
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