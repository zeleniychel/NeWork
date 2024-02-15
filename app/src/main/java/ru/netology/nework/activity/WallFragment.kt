package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.activity.dialog.LoginDialog
import ru.netology.nework.adapter.job.JobAdapter
import ru.netology.nework.adapter.post.PostAdapter
import ru.netology.nework.adapter.post.PostInteractionListener
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentUserWallBinding
import ru.netology.nework.mediaplayer.MediaLifecyclerObserver
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Post
import ru.netology.nework.model.UserResponse
import ru.netology.nework.util.getParcelableCompat
import ru.netology.nework.util.loadAttachment
import ru.netology.nework.viewmodel.WallViewModel
import javax.inject.Inject

@AndroidEntryPoint
class WallFragment : Fragment() {

    private val viewModel by viewModels<WallViewModel>()
    private val observer = MediaLifecyclerObserver()

    @Inject
    lateinit var appAuth: AppAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        lifecycle.addObserver(observer)
        val binding = FragmentUserWallBinding.inflate(layoutInflater)
        val userArg = arguments?.getParcelableCompat<UserResponse>("key")
        val adapterPost = PostAdapter(object : PostInteractionListener {

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

        val adapterJob = JobAdapter()

        if (userArg?.avatar != null) {
            binding.wallAvatar.loadAttachment(userArg.avatar)
        }
        binding.wallList.adapter = adapterPost
        viewModel.data.observe(viewLifecycleOwner) {
            adapterPost.submitList(it)
        }

        viewModel.dataJobs.observe(viewLifecycleOwner) {
            adapterJob.submitList(it)
        }
        viewModel.getWall(userArg!!.id)
        viewModel.getJobs(userArg.id)

        binding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position!!){
                    0 ->{
                        binding.wallList.adapter = adapterPost
                    }
                    1 ->{
                        binding.wallList.adapter = adapterJob

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })





        return binding.root
    }
}