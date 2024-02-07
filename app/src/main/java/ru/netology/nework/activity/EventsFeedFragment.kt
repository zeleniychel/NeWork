package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.netology.nework.R
import ru.netology.nework.adapter.EventFeedAdapter
import ru.netology.nework.adapter.OnEventInteractionListener
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentEventsFeedBinding
import ru.netology.nework.model.Event
import javax.inject.Inject

class EventsFeedFragment : Fragment() {

    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as MainActivity).supportActionBar?.title = getString(R.string.app_name)

        val binding = FragmentEventsFeedBinding.inflate(layoutInflater)


        val adapter = EventFeedAdapter(object : OnEventInteractionListener {
            override fun onEdit(event: Event) {
            }

            override fun onEvent(event: Event) {
            }

            override fun onLike(event: Event) {
            }

            override fun onRemove(event: Event) {
            }

            override fun onShare(event: Event) {
            }
        })

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_eventsFeedFragment_to_newEventFragment)
        }




        return binding.root
    }
}