package com.example.oc_realestatemanager_p9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.oc_realestatemanager_p9.app.App.Companion.db
import com.example.oc_realestatemanager_p9.database.repository.PhotoDaoRepository
import com.example.oc_realestatemanager_p9.models.Photo
import java.util.concurrent.Executor


class PhotoListViewModel(private val executor: Executor) : ViewModel() {

    private val photoRepository : PhotoDaoRepository = PhotoDaoRepository(db.photoDao())

    val photos : LiveData<List<Photo>> = photoRepository.photos

    fun getPhotoToDisplay(propertyId : Long) : LiveData<List<Photo>> = photoRepository.getPhotoToDisplay(propertyId)

    fun insertPhotos(photos : List<Photo>){
        executor.execute{
            photoRepository.insertPhotos(photos)
        }
    }

    fun deletePhotos(photos : List<Photo>){
        executor.execute{
            for (p in photos){
                photoRepository.deletePhoto(p)
            }
        }
    }
}