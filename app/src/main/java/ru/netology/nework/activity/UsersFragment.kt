package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
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
        val toolbar = (activity as AppCompatActivity).findViewById<Toolbar>(R.id.topAppBar)
        toolbar.menu.clear()

        val binding = FragmentFeedBinding.inflate(layoutInflater)
        val postIds = arguments?.getParcelableCompat<Post>("like")
        val menIds = arguments?.getParcelableCompat<Post>("men")
        val likersEventIds = arguments?.getParcelableCompat<Event>("key")
        val parIds = arguments?.getParcelableCompat<Event>("par")
        val speakersIds = arguments?.getParcelableCompat<Event>("speak")


        val adapter = UserAdapter(object : UserInteractionListener {}, null)

        binding.fab.visibility = View.GONE
        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { list ->
            val users = mutableListOf<UserResponse>()
            val addUsers = { userIds: List<Long> ->
                userIds.forEach { userId ->
                    list.find { user -> user.id == userId }?.let { users.add(it) }
                }
            }
            postIds?.likeOwnerIds?.let { addUsers(it) }?.apply { toolbar.title = getString(R.string.likers) }
            menIds?.mentionIds?.let { addUsers(it) }?.apply { toolbar.title = getString(R.string.mentioned) }
            likersEventIds?.likeOwnerIds?.let { addUsers(it) }?.apply { toolbar.title = getString(R.string.likers) }
            parIds?.participantsIds?.let { addUsers(it) }?.apply { toolbar.title = getString(R.string.participants) }
            speakersIds?.speakerIds?.let { addUsers(it) }?.apply { toolbar.title = getString(R.string.speakers) }

            adapter.submitList(users)
        }

        return binding.root
    }
}