package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import ru.netology.nework.viewmodel.AuthViewModel
import ru.netology.nework.viewmodel.UsersViewModel
import javax.inject.Inject

@AndroidEntryPoint
class UsersFeedFragment : Fragment() {

    private val viewModel by viewModels<UsersViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val toolbar = (activity as AppCompatActivity).findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = getString(R.string.app_name)
        toolbar.menu.clear()
        val binding = FragmentFeedBinding.inflate(layoutInflater)

        val adapter = UserAdapter(object : UserInteractionListener {

            override fun onUser(user: UserResponse) {
                if (appAuth.authStateFlow.value.id == user.id){
                    findNavController().navigate(R.id.action_usersFeedFragment_to_myWallFragment)
                }else{
                    findNavController().navigate(
                        R.id.action_usersFeedFragment_to_userWallFragment,
                        bundleOf("key" to user)
                    )
                }
            }
        },false)

        binding.fab.visibility = View.GONE

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        toolbar.inflateMenu(R.menu.menu_main)
        toolbar.menu.setGroupVisible(R.id.authenticated, authViewModel.authenticated)
        toolbar.menu.setGroupVisible(R.id.unauthenticated, !authViewModel.authenticated)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.sign_in -> {
                    findNavController().navigate(R.id.signInFragment)
                    true
                }

                R.id.sign_up -> {
                    findNavController().navigate(R.id.signUpFragment)
                    true
                }

                R.id.sign_out -> {

                    appAuth.removeAuth()
                    true
                }
                R.id.account -> {
                    findNavController().navigate(R.id.myWallFragment)
                    true
                }

                else -> false
            }
        }

        return binding.root
    }
}