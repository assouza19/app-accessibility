package com.example.goout.Activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.goout.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsFragment : Fragment() , OnMapReadyCallback {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_maps, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment? //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment

        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            mMap.clear() //clear old markers
            val googlePlex = CameraPosition.builder()
                .target(LatLng(37.4219999, -122.0862462))
                .zoom(10f)
                .bearing(0f)
                .tilt(45f)
                .build()
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null)
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(37.4219999, -122.0862462))
                    .title("Exemplo de estabelecimento com rampa")
                    .icon(bitmapDescriptorFromVector(context!!, R.drawable.ic_accessible_forward))
            )
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(37.4629101, -122.2449094))
                    .title("Braille")
                    .snippet("Exemplo de estabelecimento com Braille")
                    .icon(bitmapDescriptorFromVector(context!!, R.drawable.ic_hearing))
            )
        }
        return rootView
    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor? {
        val vectorDrawable =
            ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onMapReady(p0: GoogleMap?) {

    }

    companion object {
        fun newInstance(): MapsFragment =  MapsFragment()
    }
}
