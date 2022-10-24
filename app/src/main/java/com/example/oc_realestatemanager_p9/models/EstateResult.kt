package com.example.oc_realestatemanager_p9.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class EstateResult(
    @SerializedName("results")
    @Expose
    var results : List<Result>,
    @SerializedName("status")
    @Expose
    var status : String
)

data class AddressComponent(
    @SerializedName("long_name")
    @Expose
    var longName: String,

    @SerializedName("short_name")
    @Expose
    var shortName: String,

    @SerializedName("types")
    @Expose
    var types: List<String>
)

data class Result (
    @SerializedName("address_components")
    @Expose
    var addressComponents: List<AddressComponent>,

    @SerializedName("formatted_address")
    @Expose
    var formattedAddress: String,

    @SerializedName("geometry")
    @Expose
    var geometry: Geometry,

    @SerializedName("place_id")
    @Expose
    var placeId: String? = null,

    @SerializedName("plus_code")
    @Expose
    var plusCode: PlusCode? = null,

    @SerializedName("types")
    @Expose
    var types: List<String>? = null)

data class Geometry (
    @SerializedName("location")
    @Expose
    var location: Location,

    @SerializedName("location_type")
    @Expose
    var locationType: String,

    @SerializedName("viewport")
    @Expose
    var viewport: Viewport? = null)

data class Location (
    @SerializedName("lat")
    @Expose
    var lat: Double,

    @SerializedName("lng")
    @Expose
    var lng: Double)

data class PlusCode (
    @SerializedName("compound_code")
    @Expose
    var compoundCode: String? = null,

    @SerializedName("global_code")
    @Expose
    var globalCode: String? = null)

data class Viewport (
    @SerializedName("northeast")
    @Expose
    var northeast: Northeast? = null,

    @SerializedName("southwest")
    @Expose
    var southwest: Southwest? = null)

data class Southwest (
    @SerializedName("lat")
    @Expose
    var lat: Double? = null,

    @SerializedName("lng")
    @Expose
    var lng: Double? = null
)
data class Northeast (
    @SerializedName("lat")
    @Expose
    var lat: Double? = null,

    @SerializedName("lng")
    @Expose
    var lng: Double? = null)