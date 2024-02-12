package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.adapter.FeedItemAdapter
import ru.netology.nework.adapter.OnFeedItemInteractionListener
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentFeedBinding
import ru.netology.nework.mediaplayer.MediaLifecyclerObserver
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Event
import ru.netology.nework.viewmodel.EventViewModel
import javax.inject.Inject

@AndroidEntryPoint
class EventFeedFragment: Fragment() {

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

        val adapter = FeedItemAdapter(object : OnFeedItemInteractionListener<Event> {


            override fun onMedia(item: Event) {
                if (item.attachment?.type == AttachmentType.AUDIO){
                    observer.apply {
                        if (mediaPlayer?.isPlaying == true){
                            mediaPlayer?.stop()
                            mediaPlayer = null
                        } else {
                            mediaPlayer?.setDataSource(item.attachment.url)
                            observer.play()
                        }

                    }
                }

            }
        })
        binding.fab.apply {
            visibility = View.VISIBLE
            setOnClickListener{

            }
        }

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }





        return binding.root
    }
}