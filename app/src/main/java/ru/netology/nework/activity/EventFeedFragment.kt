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
import ru.netology.nework.adapter.event.EventAdapter
import ru.netology.nework.adapter.event.EventInteractionListener
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentFeedBinding
import ru.netology.nework.mediaplayer.MediaLifecyclerObserver
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Event
import ru.netology.nework.viewmodel.EventViewModel
import javax.inject.Inject

@AndroidEntryPoint
class EventFeedFragment : Fragment() {

    private val viewModel by viewModels<EventViewModel>()
    private val observer = MediaLifecyclerObserver()

    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(layoutInflater)
        lifecycle.addObserver(observer)

        val adapter = EventAdapter(object : EventInteractionListener {


            override fun onMedia(event: Event) {
                if (event.attachment?.type == AttachmentType.AUDIO) {
                    observer.apply {
                        if (mediaPlayer?.isPlaying == true) {
                            mediaPlayer?.stop()
                        } else {
                            mediaPlayer?.reset()
                            mediaPlayer?.setDataSource(event.attachment.url)
                            observer.play()
                        }
                    }
                }
            }

            override fun onEvent(event: Event) {
                findNavController().navigate(
                    R.id.action_eventFeedFragment_to_eventFragment,
                    bundleOf("key" to event)
                )
            }

            override fun onLike(event: Event) {
                if (appAuth.authStateFlow.value.id == 0L) {
                    LoginDialog().show(childFragmentManager, "")
                } else {
                    viewModel.likeEventById(event)
                }
            }
        })

        binding.fab.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                if (appAuth.authStateFlow.value.id == 0L) {
                    LoginDialog().show(childFragmentManager, "")
                } else {
                    findNavController().navigate(R.id.action_eventFeedFragment_to_newEventFragment)
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