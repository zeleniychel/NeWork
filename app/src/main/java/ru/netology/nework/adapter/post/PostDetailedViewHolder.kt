package ru.netology.nework.adapter.post

import android.net.Uri
import android.view.View
import android.widget.MediaController
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentPostBinding
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Post
import ru.netology.nework.model.UserPreview
import ru.netology.nework.util.formattedDate
import ru.netology.nework.util.load
import ru.netology.nework.util.loadAttachment

class PostDetailedViewHolder(
    private val binding: FragmentPostBinding,
    private val postInteractionListener: PostInteractionListener,
    private val authId:Long
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            imageAttachment.visibility = View.GONE
            audioAttachment.visibility = View.GONE
            videoAttachment.visibility = View.GONE
            playVideoButton.visibility = View.GONE
            if (post.authorAvatar != null) {
                avatar.load(post.authorAvatar)
            } else {
                avatar.load("")
            }
            name.text = post.author
            published.formattedDate(post.published)
            content.text = post.content
            likesCount.text = post.likeOwnerIds.size.toString()
            if (post.likeOwnerIds.contains(authId)){
                likesCount.isChecked = true
            }
            if (post.authorJob != null) {
                userJob.text = post.authorJob
            }
            mentionedCount.text = post.mentionIds.size.toString()


            //Заполнение аватаров ленты лайков
            val resultLike: MutableList<UserPreview> = mutableListOf()
            for (id in post.likeOwnerIds) {
                val key = id.toString()
                if (post.users.containsKey(key)) {
                    val user = post.users[key]
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
            for (id in post.mentionIds) {
                val key = id.toString()
                if (post.users.containsKey(key)) {
                    val user = post.users[key]
                    user?.let {
                        resultMentioned.add(it)
                    }
                }
            }
            val listMentioned = mutableListOf(
                mentionedUser0,
                mentionedUser1,
                mentionedUser2,
                mentionedUser3,
                mentionedUser4,
            )

            if (resultMentioned.size > 5) {
                mentionedUsersList.visibility = View.VISIBLE
            } else {
                mentionedUsersList.visibility = View.GONE
            }

            for (i in 0 until resultMentioned.size) {
                listMentioned[i].load(resultMentioned[i].avatar ?: "")
                listMentioned[i].visibility = View.VISIBLE
                if (i == 4) return@apply
            }

            //Вложения
            if (post.attachment != null) {
                when (post.attachment.type) {
                    AttachmentType.IMAGE ->
                        imageAttachment.apply {
                            loadAttachment(post.attachment.url)
                            visibility = View.VISIBLE
                        }

                    AttachmentType.AUDIO ->
                        audioAttachment.apply {
                            visibility = View.VISIBLE
                            setOnClickListener {
                                postInteractionListener.onMedia(post)
                            }
                        }

                    AttachmentType.VIDEO -> {
                        videoAttachment.visibility = View.VISIBLE
                        playVideoButton.visibility = View.VISIBLE
                        playVideoButton.setOnClickListener {
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
                                postInteractionListener.onMedia(post)
                            }
                        }
                    }
                }
            }
            likesCount.setOnClickListener {
                postInteractionListener.onLike(post)
            }
            root.setOnClickListener {
                postInteractionListener.onPost(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate((R.menu.options_post))
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                postInteractionListener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                postInteractionListener.onEdit(post)

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