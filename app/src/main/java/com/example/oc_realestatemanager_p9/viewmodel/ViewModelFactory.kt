package com.example.oc_realestatemanager_p9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.oc_realestatemanager_p9.app.App
import com.example.oc_realestatemanager_p9.connectivity.ConnectivityLiveDataViewModel
import com.example.oc_realestatemanager_p9.google_map.MapViewModel
import java.util.concurrent.Executor



@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val executor : Executor) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when{
            modelClass.isAssignableFrom(PropertyListViewModel::class.java) -> return PropertyListViewModel( executor) as T
            modelClass.isAssignableFrom(PhotoListViewModel::class.java) -> return PhotoListViewModel( executor) as T
            modelClass.isAssignableFrom(MapViewModel::class.java) -> return MapViewModel() as T
            modelClass.isAssignableFrom(ConnectivityLiveDataViewModel::class.java) -> return ConnectivityLiveDataViewModel(App.appContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
