package ru.netology.nework.adapter.post

import android.net.Uri
import android.view.View
import android.widget.MediaController
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.databinding.CardPostBinding
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Post
import ru.netology.nework.util.formattedDate
import ru.netology.nework.util.load
import ru.netology.nework.util.loadAttachment

class PostViewHolder(
    private val binding: CardPostBinding,
    private val postInteractionListener: PostInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
//
//    @Inject
//    lateinit var appAuth: AppAuth

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
            likes.text = post.likeOwnerIds.size.toString()
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
            root.setOnClickListener {
                postInteractionListener.onPost(post)
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