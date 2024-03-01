package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.adapter.job.myjob.MyJobAdapter
import ru.netology.nework.adapter.job.myjob.MyJobInteractionListener
import ru.netology.nework.adapter.post.PostAdapter
import ru.netology.nework.adapter.post.PostInteractionListener
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentMyWallBinding
import ru.netology.nework.mediaplayer.MediaLifecyclerObserver
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Job
import ru.netology.nework.model.Post
import ru.netology.nework.viewmodel.MyWallViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MyWallFragment : Fragment() {

    private val viewModel by viewModels<MyWallViewModel>()
    private val observer = MediaLifecyclerObserver()

    @Inject
    lateinit var appAuth: AppAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = getString(R.string.you)
        toolbar.menu.clear()

        lifecycle.addObserver(observer)
        val binding = FragmentMyWallBinding.inflate(layoutInflater)
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
                viewModel.likePostById(post)
            }

            override fun onPost(post: Post) {
                findNavController().navigate(
                    R.id.action_myWallFragment_to_postFragment,
                    bundleOf("key" to post)
                )
            }
        },null)

        val adapterJob = MyJobAdapter(object : MyJobInteractionListener {
            override fun onRemove(job: Job) {
                viewModel.deleteMyJob(job.id)
            }
        })

        toolbar.inflateMenu(R.menu.logout_menu)
        toolbar.setOnMenuItemClickListener{item ->
            when(item.itemId){
                R.id.logoutButton ->{
                    appAuth.removeAuth()
                    true
                }
                else -> false
            }
        }

        binding.wallList.adapter = adapterPost

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_myWallFragment_to_newJobFragment)
        }

        viewModel.data.observe(viewLifecycleOwner) {
            adapterPost.submitList(it)
        }

        viewModel.dataJobs.observe(viewLifecycleOwner) {
            adapterJob.submitList(it)
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position!!) {
                    0 -> {
                        binding.wallList.adapter = adapterPost
                        binding.fab.visibility = View.GONE
                    }

                    1 -> {
                        binding.wallList.adapter = adapterJob
                        binding.fab.visibility = View.VISIBLE

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