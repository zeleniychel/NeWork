package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.adapter.user.UserAdapter
import ru.netology.nework.adapter.user.UserInteractionListener
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentFeedBinding
import ru.netology.nework.model.Event
import ru.netology.nework.model.Post
import ru.netology.nework.model.UserResponse
import ru.netology.nework.util.getParcelableCompat
import ru.netology.nework.viewmodel.UsersViewModel
import javax.inject.Inject

@AndroidEntryPoint
class UsersFragment : Fragment() {

    private val viewModel by viewModels<UsersViewModel>()


    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentFeedBinding.inflate(layoutInflater)
        val likersIds = arguments?.getParcelableCompat<Post>("like")
        val menIds = arguments?.getParcelableCompat<Post>("men")
        val likersEventIds = arguments?.getParcelableCompat<Event>("key")
        val parIds = arguments?.getParcelableCompat<Event>("par")
        val speakersIds = arguments?.getParcelableCompat<Event>("speak")

        val adapter = UserAdapter(object : UserInteractionListener {})

        binding.fab.visibility = View.GONE

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { list ->
            val users = mutableListOf<UserResponse>()
            if (likersIds != null) {
                for (userId in likersIds.likeOwnerIds) {
                    list.find { user -> user.id == userId }?.let { users.add(it) }
                }
            }
            if (menIds != null) {
                for (userId in menIds.mentionIds) {
                    list.find { user -> user.id == userId }?.let { users.add(it) }
                }
            }
            if (likersEventIds != null) {
                for (userId in likersEventIds.likeOwnerIds) {
                    list.find { user -> user.id == userId }?.let { users.add(it) }
                }
            }
            if (parIds != null) {
                for (userId in parIds.participantsIds) {
                    list.find { user -> user.id == userId }?.let { users.add(it) }
                }
            }
            if (speakersIds != null) {
                for (userId in speakersIds.speakerIds) {
                    list.find { user -> user.id == userId }?.let { users.add(it) }
                }
            }

            adapter.submitList(users)
        }





        return binding.root
    }
}