package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import ru.netology.nework.R
import ru.netology.nework.adapter.OnPostInteractionListener
import ru.netology.nework.adapter.PostsFeedAdapter
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentPostsFeedBinding
import ru.netology.nework.model.Post
import javax.inject.Inject

class PostsFeedFragment: Fragment() {

    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val binding = FragmentPostsFeedBinding.inflate(layoutInflater)


        val adapter = PostsFeedAdapter(object : OnPostInteractionListener {
            override fun onEdit(post: Post) {
            }

            override fun onPost(post: Post) {
            }

            override fun onLike(post: Post) {
            }

            override fun onRemove(post: Post) {
            }

            override fun onShare(post: Post) {
            }
        })

        binding.fab.setOnClickListener{
            findNavController().navigate(R.id.action_postsFeedFragment_to_newPostFragment)
        }




        return binding.root
    }

}