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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nework.R
import ru.netology.nework.activity.dialog.LoginDialog
import ru.netology.nework.adapter.event.EventAdapter
import ru.netology.nework.adapter.event.EventInteractionListener
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentFeedBinding
import ru.netology.nework.mediaplayer.MediaLifecyclerObserver
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Event
import ru.netology.nework.viewmodel.AuthViewModel
import ru.netology.nework.viewmodel.EventViewModel
import javax.inject.Inject

@AndroidEntryPoint
class EventFeedFragment : Fragment() {

    private val viewModel by viewModels<EventViewModel>()
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
            override fun onEdit(event: Event) {
                viewModel.edit(event)
                findNavController().navigate(
                    R.id.action_eventFeedFragment_to_newEventFragment,
                    bundleOf("key" to event)
                )
            }

            override fun onRemove(event: Event) {
                viewModel.removeEventById(event.id)
            }
        }, appAuth.authStateFlow.value.id)

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
        lifecycleScope.launch {
            viewModel.data.collectLatest {
                adapter.submitData(it)
            }
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