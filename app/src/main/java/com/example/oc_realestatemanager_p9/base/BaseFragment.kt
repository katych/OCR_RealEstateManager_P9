package com.example.oc_realestatemanager_p9.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import butterknife.ButterKnife


abstract class BaseFragment : Fragment() {

    // Methods
    abstract fun getFragmentLayout() : Int


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //ButterKnife.bind(this, view)
        return inflater.inflate(getFragmentLayout(), container, false)
    }

}