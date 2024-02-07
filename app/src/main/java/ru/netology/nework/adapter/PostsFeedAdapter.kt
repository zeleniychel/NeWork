package ru.netology.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.BuildConfig
import ru.netology.nework.R
import ru.netology.nework.databinding.CardPostBinding
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Post
import ru.netology.nework.util.Converter
import ru.netology.nework.util.load
import ru.netology.nework.util.loadAttachment

class PostsFeedAdapter(
    private val onPostInteractionListener: OnPostInteractionListener
) : androidx.recyclerview.widget.ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context))
        return PostViewHolder(binding, onPostInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onPostInteractionListener: OnPostInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likes.isChecked = post.likeByMe
            likes.text = Converter.convertNumber(post.likeOwnerIds.size)
            avatar.load("${BuildConfig.BASE_URL}/api/users/${post.authorId}/avatar")
            imageAttachment.loadAttachment("${BuildConfig.BASE_URL}/api/posts/${post.id}/attachment")
            when (post.attachment.type){
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

            menu.isVisible = post.ownedByMe
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onPostInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onPostInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }


            likes.setOnClickListener {
                onPostInteractionListener.onLike(post)
            }
            share.setOnClickListener {
                onPostInteractionListener.onShare(post)
            }
            root.setOnClickListener {
                onPostInteractionListener.onPost(post)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}