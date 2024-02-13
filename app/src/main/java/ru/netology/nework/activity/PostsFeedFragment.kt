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
import ru.netology.nework.adapter.OnFeedItemInteractionListener
import ru.netology.nework.adapter.FeedItemAdapter
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentFeedBinding
import ru.netology.nework.mediaplayer.MediaLifecyclerObserver
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

        val adapter = FeedItemAdapter(object : OnFeedItemInteractionListener<Post> {

            override fun onMedia(item: Post) {
//                if (item.attachment?.type == AttachmentType.AUDIO){
//                    observer.apply {
//                        mediaPlayer?.setDataSource(item.attachment.url)
//                    }.play()
//                }
            }

            override fun onItem(item: Post) {
                findNavController().navigate(
                    R.id.action_postsFeedFragment_to_postFragment,
                    bundleOf("key" to item)
                )

            }
        })
        binding.fab.apply {
            visibility = View.VISIBLE
            setOnClickListener {

            }
        }

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }





        return binding.root
    }
}