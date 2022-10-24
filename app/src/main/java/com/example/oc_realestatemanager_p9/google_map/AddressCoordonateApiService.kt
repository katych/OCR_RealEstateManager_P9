package com.example.oc_realestatemanager_p9.google_map

import com.example.oc_realestatemanager_p9.BuildConfig
import com.example.oc_realestatemanager_p9.models.EstateResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AddressCoordonateApiService {

    @GET("json?")
    fun getCoordinateFromAddress(@Query("address") address : String,
                                    @Query("key") apiKey : String = BuildConfig.MAPS_API_KEY) : Call<EstateResult>
}