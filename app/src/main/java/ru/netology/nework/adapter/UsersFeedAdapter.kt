package ru.netology.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.BuildConfig
import ru.netology.nework.databinding.CardUserBinding
import ru.netology.nework.model.UserResponse
import ru.netology.nework.util.load

interface OnUserInteractionListener {
    fun onUser(user: UserResponse) {}
}

class UserFeedAdapter(
    private val onUserInteractionListener: OnUserInteractionListener,
) : ListAdapter<UserResponse, UserViewHolder>(UserDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = CardUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, onUserInteractionListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }
}

class UserViewHolder(
    private val binding: CardUserBinding,
    private val onUserInteractionListener: OnUserInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserResponse) {
        binding.apply {
            name.text = user.name
            login.text = user.login
            if (user.avatar != null){
                avatar.load(user.avatar)
            } else
                avatar.load("")

            root.setOnClickListener {
                onUserInteractionListener.onUser(user)
            }
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<UserResponse>() {
    override fun areItemsTheSame(oldItem: UserResponse, newItem: UserResponse): Boolean {
        if (oldItem::class != newItem::class) return false
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserResponse, newItem: UserResponse): Boolean {
        return oldItem == newItem
    }
}