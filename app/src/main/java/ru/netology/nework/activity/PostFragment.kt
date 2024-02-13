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
import ru.netology.nework.adapter.post.PostDetailedViewHolder
import ru.netology.nework.adapter.post.PostInteractionListener
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentPostBinding
import ru.netology.nework.mediaplayer.MediaLifecyclerObserver
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Post
import ru.netology.nework.util.getParcelableCompat
import ru.netology.nework.viewmodel.PostsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class PostFragment: Fragment() {

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
        val postArg = arguments?.getParcelableCompat<Post>("key")
        val binding = FragmentPostBinding.inflate(layoutInflater)

        val holder = PostDetailedViewHolder(binding,object : PostInteractionListener {

            override fun onMedia(post: Post) {
                if (post.attachment?.type == AttachmentType.AUDIO){
                    observer.apply {
                        if (mediaPlayer?.isPlaying == true){
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
                viewModel.likePostById(post)
            }
        })

        holder.bind(postArg ?: Post())
        viewModel.data.observe(viewLifecycleOwner) {
            holder.bind(it.find { (id) -> id == postArg?.id } ?: return@observe)
        }

        binding.usersList.setOnClickListener {
            findNavController().navigate(
                R.id.action_postFragment_to_usersFragment,
                bundleOf("like" to postArg))
        }
        binding.mentionedUsersList.setOnClickListener {
            findNavController().navigate(
                R.id.action_postFragment_to_usersFragment,
                bundleOf("men" to postArg))
        }

        return binding.root
    }
}