package com.example.oc_realestatemanager_p9.utils

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


object LiveDataTestUtils {

    @Suppress("UNCHECKED_CAST")
    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer: Observer<T> = object : Observer<T> {
            override fun onChanged(@Nullable o: T) {
                data[0] = o
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        return data[0] as T
    }
}