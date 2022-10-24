package com.example.oc_realestatemanager_p9.google_map

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import com.example.oc_realestatemanager_p9.R
import com.example.oc_realestatemanager_p9.models.Poi
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

import kotlinx.android.synthetic.main.window_map_infos.view.*


class EstateInfoWindowAdapter(context: Context) : GoogleMap.InfoWindowAdapter {

    private val contents: View =
        LayoutInflater.from(context).inflate(R.layout.window_map_infos, null)

    override fun getInfoContents(marker: Marker): View {
        val poi = marker.tag as Poi

        with(contents) {
            title_map_window.text = poi.name
            street_map_window.text = poi.street
            if (poi.photo.contains("document") || poi.photo.contains("images") || poi.photo.contains(
                    "https"
                )
            ) {
                image_window.setImageURI(Uri.parse(poi.photo))
            } else {
                image_window.setImageResource(poi.photo.toInt())
            }
        }
        return contents
    }

    override fun getInfoWindow(p0: Marker): View? = null


}