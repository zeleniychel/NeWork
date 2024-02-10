package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.adapter.OnUserInteractionListener
import ru.netology.nework.adapter.UserFeedAdapter
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentFeedBinding
import ru.netology.nework.model.UserResponse
import ru.netology.nework.viewmodel.UsersFeedViewModel
import javax.inject.Inject

@AndroidEntryPoint
class UsersFeedFragment : Fragment() {

    private val viewModel by viewModels<UsersFeedViewModel>()

    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(layoutInflater)

        val adapter = UserFeedAdapter(object : OnUserInteractionListener {

            override fun onUser(user: UserResponse) {

            }

        })
        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }





        return binding.root
    }
}