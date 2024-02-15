package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.adapter.user.UserAdapter
import ru.netology.nework.adapter.user.UserInteractionListener
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentFeedBinding
import ru.netology.nework.model.UserResponse
import ru.netology.nework.viewmodel.UsersViewModel
import javax.inject.Inject

@AndroidEntryPoint
class UsersFeedFragment : Fragment() {

    private val viewModel by viewModels<UsersViewModel>()

    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(layoutInflater)

        val adapter = UserAdapter(object : UserInteractionListener {

            override fun onUser(user: UserResponse) {
                findNavController().navigate(
                    R.id.action_usersFeedFragment_to_userWallFragment,
                    bundleOf("key" to user)
                )
            }
        })

        binding.fab.visibility = View.GONE

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }





        return binding.root
    }
}