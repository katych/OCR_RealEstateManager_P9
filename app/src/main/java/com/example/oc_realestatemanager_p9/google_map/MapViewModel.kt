package com.example.oc_realestatemanager_p9.google_map

import androidx.lifecycle.ViewModel
import com.example.oc_realestatemanager_p9.models.Poi
import com.example.oc_realestatemanager_p9.models.Property

import kotlinx.android.synthetic.main.activity_addproperty.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class MapViewModel : ViewModel() {

    /**
     * generate list Poi with property list
     *
     * @param properties       list
     * @return list of poi
     */
    fun generatePoi(properties : List<Property>) : MutableList<Poi> {
        val poiList: MutableList<Poi> = mutableListOf()

        for (estate in properties) {
            val p = Poi(
                name = estate.type,
                street = estate.street,
                photo = estate.photo,
                lat = estate.estateLat!!,
                long = estate.estateLong!!,
                estateId = estate.id
            )
            poiList.add(p)
        }
        return poiList
    }
}