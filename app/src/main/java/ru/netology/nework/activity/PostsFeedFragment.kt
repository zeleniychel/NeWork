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
import ru.netology.nework.activity.dialog.LoginDialog
import ru.netology.nework.adapter.post.PostInteractionListener
import ru.netology.nework.adapter.post.PostAdapter
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentFeedBinding
import ru.netology.nework.mediaplayer.MediaLifecyclerObserver
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Post
import ru.netology.nework.viewmodel.PostsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class PostsFeedFragment : Fragment() {

    private val viewModel by viewModels<PostsViewModel>()
    private val observer = MediaLifecyclerObserver()

    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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



        return binding.root
    }
}