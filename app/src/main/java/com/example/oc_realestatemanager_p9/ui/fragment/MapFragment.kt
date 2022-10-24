package com.example.oc_realestatemanager_p9.ui.fragment

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.oc_realestatemanager_p9.R
import com.example.oc_realestatemanager_p9.base.BaseFragment
import com.example.oc_realestatemanager_p9.connectivity.ConnectivityLiveDataViewModel
import com.example.oc_realestatemanager_p9.google_map.EstateInfoWindowAdapter
import com.example.oc_realestatemanager_p9.google_map.MapViewModel
import com.example.oc_realestatemanager_p9.models.Poi
import com.example.oc_realestatemanager_p9.models.Property
import com.example.oc_realestatemanager_p9.utils.Utils
import com.example.oc_realestatemanager_p9.viewmodel.Injection
import com.example.oc_realestatemanager_p9.viewmodel.PropertyListViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_map.*
import timber.log.Timber
import java.util.*

/**
 * A simple [Fragment] subclass.
 */

class MapFragment: BaseFragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    interface OnClickMarkerListener{
        fun onClickMarkerEstate(propertyId : Long)
    }

    //Fields
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mapOptions: GoogleMapOptions
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mMapViewModel: MapViewModel
    private lateinit var estateViewModel : PropertyListViewModel
    private lateinit var networkConnection : ConnectivityLiveDataViewModel

    private val factory = Injection.provideViewModelFactory()
    private var estateList : MutableList<Property> = mutableListOf()
    private var poiList : MutableList<Poi> = mutableListOf()
    private var listener : OnClickMarkerListener? = null
    private var lastPosition: LatLng? = null


    override fun getFragmentLayout(): Int {
        return R.layout.fragment_map
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //NetWork enable verification

        networkConnection =
            ViewModelProvider(this, factory).get(ConnectivityLiveDataViewModel::class.java)

        mapOptions = GoogleMapOptions()
            .mapType(GoogleMap.MAP_TYPE_NORMAL)
            .zoomControlsEnabled(true)
            .zoomGesturesEnabled(true)

    }



    private fun getConnexion( isConnected : Boolean) {
        if (isConnected){
            initMap()
        }else{
            Utils.makeSnackBar(content_map, getString(R.string.no_internet))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Location Services
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(requireContext()))

        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        networkConnection.isConnected.observe(viewLifecycleOwner, androidx.lifecycle.Observer { it -> getConnexion(it) })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClickMarkerListener){
            listener = context
        }else{
            throw RuntimeException("$context must implemente PropertyListFragment interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    override fun onMapReady(map: GoogleMap) {
        mGoogleMap = map
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(),R.raw.maps_style))
        mGoogleMap.setInfoWindowAdapter(EstateInfoWindowAdapter(requireContext()))
        Timber.i("Map ready")

        estateViewModel = ViewModelProvider(this, factory).get(PropertyListViewModel::class.java)
        estateViewModel.properties.observe(viewLifecycleOwner, androidx.lifecycle.Observer { properties -> getEstates(properties) })
        mMapViewModel = ViewModelProvider(this, factory).get(MapViewModel::class.java)

        getLocationPermission()
    }

    private fun getEstates(properties: List<Property>) {
        estateList.clear()
        estateList.addAll(properties)
    }

    private fun getLocationPermission() {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    //Get last location and update UI
                    getUserLocation()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Utils.makeSnackBar(map_Fragment, "Permissions is needed for the map")
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?,
                ) {
                }
            }).check()
    }


    private fun getUserLocation() {
        mFusedLocationClient.lastLocation
            .addOnSuccessListener(Objects.requireNonNull(requireActivity())) { location ->
                if (location != null) {
                    // get the location phone
                    lastPosition = LatLng(location.latitude, location.longitude)
                    //lastPosition = LatLng(40.7808, -73.9772)
                    Timber.i("LastLocation : $lastPosition")
                    //Update UI with information
                    updateUiMap(lastPosition!!)
                }
            }
    }

    private fun updateUiMap(lastPosition: LatLng) {

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPosition, 11f))
        //set my position and enable position button
        mGoogleMap.isMyLocationEnabled = true
        //get poi estate
        poiList = mMapViewModel.generatePoi(estateList)
        Timber.d("list poi= ${poiList.size}, t $poiList")
        generateEstatePoi(poiList)
    }
    /**
     * generate marker with poi list
     *
     * @param poi list
     */
    private fun generateEstatePoi(poi: MutableList<Poi>) {

        for (p in poi) {
            setMarkerPoi(p, mGoogleMap)
            mGoogleMap.setOnInfoWindowClickListener(this)
        }
    }
    private fun setMarkerPoi(poi: Poi, map: GoogleMap) {
        val markerOptions = MarkerOptions()
            .position(LatLng(poi.lat, poi.long))
            .title(poi.name)
            .snippet(poi.street)

        val marker = map.addMarker(markerOptions)
        //Add tag to save restaurant placeId for earlier
        if (poi.estateId > 0){
            if (marker != null) {
                marker.tag = poi
            }
            if (marker != null) {
                marker.zIndex = poi.estateId.toFloat()
            }
            if (marker != null) {
                marker.title = poi.name
            }
        }
    }
    //init map
    private fun initMap() {
        Timber.d("initMap: initializing map")
        val mapFragment = SupportMapFragment.newInstance(mapOptions)

        fragmentManager?.beginTransaction()
            ?.replace(R.id.content_map, mapFragment)
            ?.commit()

        mapFragment!!.getMapAsync(this)
    }

    override fun onInfoWindowClick(marker: Marker) {
        launchEstateDetail(marker)
    }

    private fun launchEstateDetail(marker: Marker) {
        listener?.onClickMarkerEstate(marker.zIndex.toLong())
    }
}