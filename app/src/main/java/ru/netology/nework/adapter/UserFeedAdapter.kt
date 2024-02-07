package ru.netology.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.BuildConfig
import ru.netology.nework.databinding.CardUserPreviewBinding
import ru.netology.nework.model.UserResponse
import ru.netology.nework.util.load


class UserFeedAdapter(
    private val onUserInteractionListener: OnUserInteractionListener
) : androidx.recyclerview.widget.ListAdapter<UserResponse, UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = CardUserPreviewBinding.inflate(LayoutInflater.from(parent.context))
        return UserViewHolder(binding, onUserInteractionListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }
}

class UserViewHolder(
    private val binding: CardUserPreviewBinding,
    private val onUserInteractionListener: OnUserInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserResponse) {
        binding.apply {
            author.text = user.name
            userLogin.text = user.login
            avatar.load("${BuildConfig.BASE_URL}/api/users/${user.id}/avatar")


            root.setOnClickListener {
                onUserInteractionListener.onUser(user)
            }
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<UserResponse>() {
    override fun areItemsTheSame(oldItem: UserResponse, newItem: UserResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserResponse, newItem: UserResponse): Boolean {
        return oldItem == newItem
    }
}