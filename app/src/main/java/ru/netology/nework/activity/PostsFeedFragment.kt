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
import ru.netology.nework.activity.dialog.LoginDialog
import ru.netology.nework.adapter.post.PostInteractionListener
import ru.netology.nework.adapter.post.PostAdapter
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentFeedBinding
import ru.netology.nework.mediaplayer.MediaLifecyclerObserver
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Post
import ru.netology.nework.viewmodel.AuthViewModel
import ru.netology.nework.viewmodel.PostsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class PostsFeedFragment : Fragment() {

    private val viewModel by viewModels<PostsViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    private val observer = MediaLifecyclerObserver()

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
        lifecycle.addObserver(observer)
        val binding = FragmentFeedBinding.inflate(layoutInflater)

        val adapter = PostAdapter(object : PostInteractionListener {

            override fun onMedia(post: Post) {
                if (post.attachment?.type == AttachmentType.AUDIO) {
                    observer.apply {
                        if (mediaPlayer?.isPlaying == true) {
                            mediaPlayer?.stop()
                        } else {
                            mediaPlayer?.reset()
                            mediaPlayer?.setDataSource(post.attachment.url)
                            observer.play()
                        }

                    }
                }
            }

            override fun onLike(post: Post) {
                if (appAuth.authStateFlow.value.id == 0L) {
                    LoginDialog().show(childFragmentManager, "")
                } else {
                    viewModel.likePostById(post)
                }
            }

            override fun onEdit(post: Post) {
                super.onEdit(post)
            }

            override fun onRemove(post: Post) {
                viewModel.removePostById(post.id)
            }

            override fun onPost(post: Post) {
                findNavController().navigate(
                    R.id.action_postsFeedFragment_to_postFragment,
                    bundleOf("key" to post)
                )

            }
        })

        binding.fab.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                if (appAuth.authStateFlow.value.id == 0L) {
                    LoginDialog().show(childFragmentManager, "")
                } else {
                    findNavController().navigate(R.id.action_postsFeedFragment_to_newPostFragment)
                }
            }
        }

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