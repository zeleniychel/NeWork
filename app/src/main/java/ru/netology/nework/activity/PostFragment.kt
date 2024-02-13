package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentPostBinding
import ru.netology.nework.mediaplayer.MediaLifecyclerObserver
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

//        val holder = PostViewHolder(binding,object : OnFeedItemInteractionListener<Post> {
//
//            override fun onMedia(item: Post) {
////                if (item.attachment?.type == AttachmentType.AUDIO){
////                    observer.apply {
////                        mediaPlayer?.setDataSource(item.attachment.url)
////                    }.play()
////                }
//            }
//        })
//
//        binding.apply {
//            userJob.text = postArg?.authorJob
//        }
//        holder.bind(postArg ?: Post())
//        viewModel.data.observe(viewLifecycleOwner) {
//            holder.bind(it.posts.find { (id) -> id == postArg?.id } ?: Post())
//        }






        return binding.root
    }
}