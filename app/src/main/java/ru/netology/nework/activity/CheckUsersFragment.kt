package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.adapter.user.UserAdapter
import ru.netology.nework.adapter.user.UserInteractionListener
import ru.netology.nework.databinding.FragmentFeedBinding
import ru.netology.nework.viewmodel.PostsViewModel
import ru.netology.nework.viewmodel.UsersViewModel

@AndroidEntryPoint
class CheckUsersFragment : Fragment() {

    private val viewModel by viewModels<UsersViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = getString(R.string.choose_users)
        toolbar.menu.clear()

        val binding = FragmentFeedBinding.inflate(layoutInflater)
        val users = mutableListOf<Long>()
        val adapter = UserAdapter(object : UserInteractionListener {

            override fun onCheck(userId: Long) {
                if(users.any{it != userId}){
                    users.remove(userId)
                } else {
                    users.add(userId)
                }
            }
        }, true)

        binding.fab.visibility = View.GONE
        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        toolbar.inflateMenu(R.menu.save_menu)
        toolbar.setOnMenuItemClickListener{item ->
            when(item.itemId){
                R.id.save ->{
                    viewModel.saveUsers(users)

                    true
                }
                else -> false
            }
        }
        return binding.root
    }
}