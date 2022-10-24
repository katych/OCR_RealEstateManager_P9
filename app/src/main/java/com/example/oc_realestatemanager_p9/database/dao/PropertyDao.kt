package com.example.oc_realestatemanager_p9.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.oc_realestatemanager_p9.models.Photo
import com.example.oc_realestatemanager_p9.models.Property


@Dao
interface PropertyDao {

    @Query("SELECT * FROM estate")
    fun getAllProperties() : LiveData<List<Property>>

    @Query("SELECT * FROM estate WHERE id = :propertyId")
    fun getProperty(propertyId : Long) : LiveData<Property>

    @RawQuery(observedEntities = [Property::class])
    fun getPropertyWithFilter(query: SupportSQLiteQuery) : LiveData<List<Property>>

    @Insert
    fun createProperty(properties : Property) : Long

    @Update
    fun updateProperty(property : Property) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProperties(property: List<Property>)

    @Delete
    fun deleteProperty(property : Property)

    // Content provider
    @Query("SELECT * FROM estate")
    fun getAllPropertiesWithCursor() : Cursor

    @Query("SELECT * FROM estate WHERE id = :propertyId")
    fun getPropertyByIdWithCursor(propertyId : Long) : Cursor

    @Query("DELETE FROM estate WHERE id LIKE :propertyId")
    fun deletePropertyById(propertyId: Long) : Int
}