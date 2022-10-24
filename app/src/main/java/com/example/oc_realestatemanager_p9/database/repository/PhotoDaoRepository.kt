package com.example.oc_realestatemanager_p9.database.repository

import androidx.lifecycle.LiveData
import com.example.oc_realestatemanager_p9.database.dao.PhotoDao
import com.example.oc_realestatemanager_p9.models.Photo


class PhotoDaoRepository(private val photoDao: PhotoDao) {

    val photos : LiveData<List<Photo>> = photoDao.getAllPhotos()

    fun getPhotoToDisplay(propertyId : Long) : LiveData<List<Photo>> = photoDao.getPhotosForPropertyId(propertyId)

    fun insertPhotos(photos : List<Photo>) = photoDao.insertPhotos(photos)

    fun updatePhoto(photo : Photo) = photoDao.updatePhotos(photo)

    fun deletePhoto(photo : Photo) = photoDao.deletePhoto(photo)
}