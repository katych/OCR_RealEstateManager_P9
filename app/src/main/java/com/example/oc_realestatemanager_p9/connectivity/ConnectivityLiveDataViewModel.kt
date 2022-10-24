package com.example.oc_realestatemanager_p9.connectivity

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.oc_realestatemanager_p9.utils.Utils

class ConnectivityLiveDataViewModel(val context: Context) : ViewModel() {

    val isConnected: LiveData<Boolean> = Utils.isNetworkAvailable(context)

}
