package com.example.oc_realestatemanager_p9.google_map

import com.example.oc_realestatemanager_p9.models.EstateResult
import com.google.android.gms.maps.model.LatLng


fun mapGeocodingDataToLatLong(estateResult: EstateResult) : LatLng{
    val estateFirst = estateResult.results.first()

    return LatLng(
        estateFirst.geometry.location.lat,
        estateFirst.geometry.location.lng
    )
}