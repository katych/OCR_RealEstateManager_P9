package com.example.oc_realestatemanager_p9.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.oc_realestatemanager_p9.models.Photo


@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo")
    fun getAllPhotos() : LiveData<List<Photo>>

    @Query("""SELECT * FROM photo
                    JOIN estate ON estate.id = property_id
                    WHERE estate.id = :propertyId
    """)
    fun getPhotosForPropertyId(propertyId : Long) : LiveData<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotos(photos : List<Photo>)

    @Insert
    fun insertPhoto(photo : Photo)

    @Update
     fun updatePhotos(photos : Photo)

    @Delete
    fun deletePhotosList(photo : List<Photo>)

    @Delete
    fun deletePhoto(photo : Photo)
}