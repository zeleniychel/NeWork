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
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.adapter.event.EventDetailedViewHolder
import ru.netology.nework.adapter.event.EventInteractionListener
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentEventBinding
import ru.netology.nework.mediaplayer.MediaLifecyclerObserver
import ru.netology.nework.model.AttachmentType
import ru.netology.nework.model.Event
import ru.netology.nework.util.getParcelableCompat
import ru.netology.nework.viewmodel.EventViewModel
import javax.inject.Inject

@AndroidEntryPoint
class EventFragment: Fragment() {

    private val viewModel by viewModels<EventViewModel>()
    private val observer = MediaLifecyclerObserver()

    @Inject
    lateinit var appAuth: AppAuth
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = getString(R.string.event)
        toolbar.menu.clear()

        lifecycle.addObserver(observer)
        val eventArg = arguments?.getParcelableCompat<Event>("key")
        val binding = FragmentEventBinding.inflate(layoutInflater)

        MapKitFactory.initialize(requireContext())
        mapView = binding.mapView

        if (eventArg?.coords != null){
            mapView.visibility = View.VISIBLE
            mapView.mapWindow.map.apply {
                move(
                    CameraPosition(
                        Point(
                            eventArg.coords.lat,
                            eventArg.coords.long),
                        17.0F,
                        0.0F,
                        0.0F)
                )
                mapObjects.addPlacemark().apply {
                    geometry = Point(
                        eventArg.coords.lat,
                        eventArg.coords.long)
                    setIcon(ImageProvider.fromResource(requireContext(),R.drawable.pin))
                }
            }
        }

        val holder = EventDetailedViewHolder(binding,object : EventInteractionListener {

            override fun onMedia(event:Event) {
                if (event.attachment?.type == AttachmentType.AUDIO){
                    observer.apply {
                        if (mediaPlayer?.isPlaying == true){
                            mediaPlayer?.stop()
                        } else {
                            mediaPlayer?.reset()
                            mediaPlayer?.setDataSource(event.attachment.url)
                            observer.play()
                        }

                    }
                }
            }

            override fun onLike(event: Event) {
                viewModel.likeEventById(event)
            }
        })

        holder.bind(eventArg ?: Event())
        viewModel.data.observe(viewLifecycleOwner) {
            holder.bind(it.find { (id) -> id == eventArg?.id } ?: return@observe)
        }

        binding.usersList.setOnClickListener {
            findNavController().navigate(
                R.id.action_eventFragment_to_usersFragment,
                bundleOf("key" to eventArg)
            )
        }
        binding.participantUsersList.setOnClickListener {
            findNavController().navigate(
                R.id.action_eventFragment_to_usersFragment,
                bundleOf("par" to eventArg)
            )
        }
        binding.participantUsersList.setOnClickListener {
            findNavController().navigate(
                R.id.action_eventFragment_to_usersFragment,
                bundleOf("speak" to eventArg)
            )
        }
        toolbar.inflateMenu(R.menu.share_menu)
        toolbar.setOnMenuItemClickListener{item ->
            when(item.itemId){
                R.id.share ->{
                    true
                }
                else -> false
            }
        }

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
        mapView.onStop()
    }
}