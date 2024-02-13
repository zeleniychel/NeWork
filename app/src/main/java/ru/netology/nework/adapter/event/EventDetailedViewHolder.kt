package ru.netology.nework.adapter.event

import android.net.Uri
import android.view.View
import android.widget.MediaController
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.databinding.FragmentEventBinding
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Event
import ru.netology.nework.model.UserPreview
import ru.netology.nework.util.formattedDate
import ru.netology.nework.util.load
import ru.netology.nework.util.loadAttachment

class EventDetailedViewHolder(
    private val binding: FragmentEventBinding,
    private val eventInteractionListener: EventInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
//
//    @Inject
//    lateinit var appAuth: AppAuth

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
            eventDate.formattedDate(event.published)
            content.text = event.content
            likesCount.text = event.likeOwnerIds.size.toString()
            likesCount.isChecked = event.likeByMe
            if (event.authorJob != null){
                userJob.text = event.authorJob
            }
            participantsCount.text = event.participantsIds.size.toString()


            //Заполнение аватаров ленты лайков
            val resultLike: MutableList<UserPreview> = mutableListOf()
            for (id in event.likeOwnerIds) {
                val key = id.toString()
                if (event.users.containsKey(key)) {
                    val user = event.users[key]
                    user?.let {
                        resultLike.add(it)
                    }
                }
            }
            val listLike = mutableListOf(
                user0,
                user1,
                user2,
                user3,
                user4,
            )

            if (resultLike.size > 5) {
                usersList.visibility = View.VISIBLE
            } else {
                usersList.visibility = View.GONE
            }

            for (i in 0 until resultLike.size) {
                listLike[i].load(resultLike[i].avatar ?: "")
                listLike[i].visibility = View.VISIBLE
                if (i == 4) return@apply
            }

//Заполнение аватаров ленты упомянутых
            val resultMentioned: MutableList<UserPreview> = mutableListOf()
            for (id in event.participantsIds) {
                val key = id.toString()
                if (event.users.containsKey(key)) {
                    val user = event.users[key]
                    user?.let {
                        resultMentioned.add(it)
                    }
                }
            }
            val listMentioned = mutableListOf(
                participantUser0,
                participantUser1,
                participantUser2,
                participantUser3,
                participantUser4,
            )

            if (resultMentioned.size > 5) {
                participantUsersList.visibility = View.VISIBLE
            } else {
                participantUsersList.visibility = View.GONE
            }

            for (i in 0 until resultMentioned.size) {
                listMentioned[i].load(resultMentioned[i].avatar ?: "")
                listMentioned[i].visibility = View.VISIBLE
                if (i == 4) return@apply
            }

            //Вложения
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
            likesCount.setOnClickListener {
                eventInteractionListener.onEvent(event)
            }
            root.setOnClickListener {
                eventInteractionListener.onEvent(event)
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