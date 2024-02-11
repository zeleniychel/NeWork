package ru.netology.nework.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.R
import ru.netology.nework.databinding.CardEventBinding
import ru.netology.nework.databinding.CardPostBinding
import ru.netology.nework.databinding.CardUserBinding
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Event
import ru.netology.nework.model.FeedItem
import ru.netology.nework.model.Post
import ru.netology.nework.model.UserResponse
import ru.netology.nework.util.formattedDate
import ru.netology.nework.util.load
import ru.netology.nework.util.loadAttachment

interface OnFeedItemInteractionListener<T> {
    fun onItem(item: T) {}
    fun onLike(item: T) {}
    fun onMedia(item: T) {}
    fun onEdit(item: T) {}
    fun onRemove(item: T) {}
}


class FeedItemAdapter<T>(
    private val onFeedItemInteractionListener: OnFeedItemInteractionListener<T>,
) : ListAdapter<FeedItem, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is Event -> R.layout.card_event
            is Post -> R.layout.card_post
            is UserResponse -> R.layout.card_user
            null -> error("unknown item type")
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.card_user -> {
                val binding =
                    CardUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                UserViewHolder(binding)
            }

            R.layout.card_post -> {
                val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PostViewHolder(binding, onFeedItemInteractionListener as OnFeedItemInteractionListener<Post>)
            }

            R.layout.card_event -> {
                val binding = CardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EventViewHolder(binding, onFeedItemInteractionListener as OnFeedItemInteractionListener<Event>)
            }

            else -> error("unknown item type")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is Event -> (holder as? EventViewHolder)?.bind(item)
            is Post -> (holder as? PostViewHolder)?.bind(item)
            is UserResponse -> (holder as? UserViewHolder)?.bind(item)
            null -> error("unknown item type")
        }
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onFeedItemInteractionListener: OnFeedItemInteractionListener<Post>
) : RecyclerView.ViewHolder(binding.root) {
//
//    @Inject
//    lateinit var appAuth: AppAuth

    fun bind(post: Post) {
        binding.apply {
            if (post.authorAvatar != null) {
                avatar.load(post.authorAvatar)
            } else {
                avatar.load("")
            }
            name.text = post.author
            published.formattedDate(post.published)
            content.text = post.content
            likes.text = post.likeOwnerIds.size.toString()
            if (post.attachment != null) {
                imageAttachment.visibility = View.GONE
                audioAttachment.visibility = View.GONE
                group.visibility = View.GONE
                when (post.attachment.type) {
                    AttachmentType.IMAGE ->
                        imageAttachment.apply {
                            loadAttachment(post.attachment.url)
                            visibility = View.VISIBLE
                        }

                    AttachmentType.AUDIO ->
                        audioAttachment.apply {
                            visibility = View.VISIBLE
                            onFeedItemInteractionListener.onMedia(post)
                        }

                    AttachmentType.VIDEO ->
                        group.apply {
                            playVideoButton.apply {
                                visibility = View.GONE
                                setOnClickListener {
                                    videoAttachment.apply {
                                        setMediaController(MediaController(context))
                                        setVideoURI(
                                            Uri.parse(post.attachment.url)
                                        )
                                        setOnPreparedListener {
                                            start()
                                        }
                                        setOnCompletionListener {
                                            stopPlayback()
                                        }
                                        onFeedItemInteractionListener.onMedia(post)
                                    }
                                }
                            }

                        }
                }
            }
//            if (appAuth.authStateFlow.value.id == post.authorId) {
//                menu.visibility = View.VISIBLE
//                menu.setOnClickListener {
//                    PopupMenu(it.context, it).apply {
//                        inflate((R.menu.options_post))
//                        setOnMenuItemClickListener { item ->
//                            when (item.itemId) {
//                                R.id.remove -> {
//                                    true
//                                }
//
//                                R.id.edit -> {
//
//                                    true
//                                }
//
//                                else -> false
//                            }
//                        }
//                    }.show()
//                }
//            }
        }
    }
}

class EventViewHolder(
    private val binding: CardEventBinding,
    private val onFeedItemInteractionListener: OnFeedItemInteractionListener<Event>
) : RecyclerView.ViewHolder(binding.root) {
//
//    @Inject
//    lateinit var appAuth: AppAuth

    fun bind(event: Event) {
        binding.apply {
            imageAttachment.visibility = View.GONE
            audioAttachment.visibility = View.GONE
            group.visibility = View.GONE

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

            if (event.attachment != null) {
                imageAttachment.visibility = View.GONE
                audioAttachment.visibility = View.GONE
                group.visibility = View.GONE
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
                                onFeedItemInteractionListener.onMedia(event)
                            }
                        }

                    AttachmentType.VIDEO ->
                        group.apply {
                            visibility = View.VISIBLE
                            playVideoButton.apply {
                                setOnClickListener {
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
                                        onFeedItemInteractionListener.onMedia(event)
                                    }
                                }
                            }

                        }
                }
            }

//            if (appAuth.authStateFlow.value.id == post.authorId) {
//                menu.visibility = View.VISIBLE
//                menu.setOnClickListener {
//                    PopupMenu(it.context, it).apply {
//                        inflate((R.menu.options_post))
//                        setOnMenuItemClickListener { item ->
//                            when (item.itemId) {
//                                R.id.remove -> {
//                                    true
//                                }
//
//                                R.id.edit -> {
//
//                                    true
//                                }
//
//                                else -> false
//                            }
//                        }
//                    }.show()
//                }
//            }
        }
    }
}

class UserViewHolder(
    private val binding: CardUserBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserResponse) {
        binding.apply {
            name.text = user.name
            login.text = user.login
            if (user.avatar != null) {
                avatar.load(user.avatar)
            } else
                avatar.load("")
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<FeedItem>() {
    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        if (oldItem::class != newItem::class) return false
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem == newItem
    }
}