package ru.netology.nework.adapter.user

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.databinding.CardUserBinding
import ru.netology.nework.model.UserResponse
import ru.netology.nework.util.load

class UserViewHolder(
    private val binding: CardUserBinding,
    private val userInteractionListener: UserInteractionListener,
    private val checkBox: Boolean?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserResponse) {
        binding.apply {
            name.text = user.name
            login.text = user.login
            if (user.avatar != null) {
                avatar.load(user.avatar)
            } else
                avatar.load("")

            root.setOnClickListener{
                userInteractionListener.onUser(user)
            }
            if (checkBox == true){
                checkbox.visibility = View.VISIBLE
            }
            checkbox.setOnClickListener{
                userInteractionListener.onCheck(user.id)
            }

        }
    }
}