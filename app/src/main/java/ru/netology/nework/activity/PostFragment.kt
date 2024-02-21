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
class PostFragment : Fragment() {

    private val viewModel by viewModels<PostsViewModel>()
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
        toolbar.title = getString(R.string.post)
        toolbar.menu.clear()
        lifecycle.addObserver(observer)
        val postArg = arguments?.getParcelableCompat<Post>("key")
        val binding = FragmentPostBinding.inflate(layoutInflater)

        MapKitFactory.initialize(requireContext())
        mapView = binding.mapView

        if (postArg?.coords != null){
            mapView.visibility = View.VISIBLE
            mapView.mapWindow.map.apply {
                move(
                    CameraPosition(
                        Point(
                            postArg.coords.lat,
                            postArg.coords.long),
                        17.0F,
                        0.0F,
                        0.0F))
                mapObjects.addPlacemark().apply {
                    geometry = Point(
                        postArg.coords.lat,
                        postArg.coords.long)
                    setIcon(ImageProvider.fromResource(requireContext(),R.drawable.pin))
                }
            }
        }


            val holder = PostDetailedViewHolder (binding, object : PostInteractionListener {

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
        })

        holder.bind(postArg ?: Post())
        viewModel.data.observe(viewLifecycleOwner) {
            holder.bind(it.find { (id) -> id == postArg?.id } ?: return@observe)
        }

        binding.usersList.setOnClickListener {
            findNavController().navigate(
                R.id.action_postFragment_to_usersFragment,
                bundleOf("like" to postArg)
            )
        }
        binding.mentionedUsersList.setOnClickListener {
            findNavController().navigate(
                R.id.action_postFragment_to_usersFragment,
                bundleOf("men" to postArg)
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