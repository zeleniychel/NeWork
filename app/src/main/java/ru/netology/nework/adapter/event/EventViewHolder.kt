package ru.netology.nework.adapter.event

import android.net.Uri
import android.view.View
import android.widget.MediaController
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.R
import ru.netology.nework.databinding.CardEventBinding
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Event
import ru.netology.nework.util.formattedDate
import ru.netology.nework.util.load
import ru.netology.nework.util.loadAttachment

class EventViewHolder(
    private val binding: CardEventBinding,
    private val eventInteractionListener: EventInteractionListener,
    private val authId: Long?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(event: Event) {
        binding.apply {
            imageAttachment.visibility = View.GONE
            audioAttachment.visibility = View.GONE
            videoAttachment.visibility = View.GONE
            playVideoButton.visibility = View.GONE

            if (event.authorAvatar != null) {
                avatar.load(event.authorAvatar)
            } else {
                avatar.load("")
            }
            name.text = event.author
            published.formattedDate(event.published)
            content.text = event.content
            eventType.text = event.type.name
            eventDate.formattedDate(event.datetime)
            participants.text = event.participantsIds.size.toString()
            likes.text = event.likeOwnerIds.size.toString()
            likes.isChecked = event.likeByMe

            if (event.attachment != null) {
                when (event.attachment.type) {
                    AttachmentType.IMAGE ->
                        imageAttachment.apply {
                            loadAttachment(event.attachment.url)
                            visibility = View.VISIBLE
                        }

                    AttachmentType.AUDIO ->
                        audioAttachment.apply {
                            visibility = View.VISIBLE
                            setOnClickListener {
                                eventInteractionListener.onMedia(event)
                            }
                        }

                    AttachmentType.VIDEO -> {
                        videoAttachment.visibility = View.VISIBLE
                        playVideoButton.visibility = View.VISIBLE
                        playVideoButton.setOnClickListener {
                            videoAttachment.apply {
                                setMediaController(MediaController(context))
                                setVideoURI(
                                    Uri.parse(event.attachment.url)
                                )
                                setOnPreparedListener {
                                    start()
                                }
                                setOnCompletionListener {
                                    stopPlayback()
                                }
                                eventInteractionListener.onMedia(event)
                            }
                        }
                    }
                }
            }

            likes.setOnClickListener {
                eventInteractionListener.onLike(event)
            }

            root.setOnClickListener {
                eventInteractionListener.onEvent(event)
            }
            if (authId == event.authorId) {
                menu.visibility = View.VISIBLE
                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate((R.menu.options_post))
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    eventInteractionListener.onRemove(event)
                                    true
                                }

                                R.id.edit -> {
                                    eventInteractionListener.onEdit(event)

                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }
            }
        }
    }
}