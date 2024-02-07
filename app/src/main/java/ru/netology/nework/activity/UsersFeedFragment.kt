package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.netology.nework.R
import ru.netology.nework.adapter.OnUserInteractionListener
import ru.netology.nework.adapter.UserFeedAdapter
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentUsersFeedBinding
import ru.netology.nework.model.UserResponse
import ru.netology.nework.viewmodel.UserViewModel
import javax.inject.Inject

@AndroidEntryPoint
class UsersFeedFragment : Fragment() {

    private val viewModel by viewModels<UserViewModel>()


    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as MainActivity).supportActionBar?.title = getString(R.string.registration)

        val binding = FragmentUsersFeedBinding.inflate(layoutInflater)


        val adapter = UserFeedAdapter(object : OnUserInteractionListener {
            override fun onUser(user: UserResponse) {
                findNavController().navigate(R.id.action_usersFeedFragment_to_userFragment)
            }

        })
        binding.list.adapter = adapter

            viewModel.data.observe(viewLifecycleOwner) { users ->
                adapter.submitList(users)
            }

        return binding.root
    }
}