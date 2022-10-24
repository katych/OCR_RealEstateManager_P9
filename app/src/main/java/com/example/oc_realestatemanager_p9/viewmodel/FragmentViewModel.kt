package com.example.oc_realestatemanager_p9.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FragmentViewModel: ViewModel() {
    val data = MutableLiveData<String>()

    fun setData( newData : String){
        data.value = newData
    }
}