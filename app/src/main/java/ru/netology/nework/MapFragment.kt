package ru.netology.nework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import ru.netology.nework.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val binding = FragmentMapBinding.inflate(layoutInflater)

        MapKitFactory.initialize(requireContext())
        mapView = binding.newItemMap

        mapView.mapWindow.map.apply {
            move(
                CameraPosition(
                    Point(56.314137,43.991497),
                    17.0F,
                    0.0F,
                    0.0F
                )
            )
        }
        val inputListener = object : InputListener {
            override fun onMapTap(p0: Map, p1: Point) {
            }

            override fun onMapLongTap(p0: Map, p1: Point) {
                TODO("Not yet implemented")
            }
        }
        mapView.mapWindow.map.addInputListener(inputListener)



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