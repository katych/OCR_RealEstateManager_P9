package com.example.oc_realestatemanager_p9.viewmodel

import java.util.concurrent.Executor
import java.util.concurrent.Executors


class Injection {

    companion object{

        private fun provideExecutor() : Executor{
            return Executors.newSingleThreadExecutor()
        }

        fun provideViewModelFactory() : ViewModelFactory{
            val executor = provideExecutor()
            return ViewModelFactory(executor)
        }

    }
}