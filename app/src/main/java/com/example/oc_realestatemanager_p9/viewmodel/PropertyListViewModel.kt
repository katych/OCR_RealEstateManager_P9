package com.example.oc_realestatemanager_p9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.oc_realestatemanager_p9.app.App
import com.example.oc_realestatemanager_p9.app.App.Companion.db
import com.example.oc_realestatemanager_p9.database.repository.PropertyDaoRepository
import com.example.oc_realestatemanager_p9.models.Photo
import com.example.oc_realestatemanager_p9.models.Property
import java.util.concurrent.Executor


class PropertyListViewModel(private val executor: Executor) : ViewModel() {

    private val repositoryProperty: PropertyDaoRepository

    init {
        val propertyDao =App.db.propertyDao()
        repositoryProperty = PropertyDaoRepository(propertyDao)
    }

    var properties: LiveData<List<Property>> = repositoryProperty.properties

    fun getPropertyById(propertyId: Long): LiveData<Property> =
        repositoryProperty.getPropertyById(propertyId)

    fun createProperty(property: Property, photos: List<Photo>) {
        executor.execute {
            val long = repositoryProperty.createProperty(property)
            for (p in photos) {
                p.propertyId = long
            }
        }
    }

    fun updateProperty(property: Property, photos: List<Photo>) {
        executor.execute {
            for (p in photos) {
                p.propertyId = property.id
            }
            repositoryProperty.updateProperty(property)
        }
    }

    /**************************************************************************************************
     * Filter method
     *************************************************************************************************/

    fun getPropertyWithFilter(query : SupportSQLiteQuery) : LiveData<List<Property>> = repositoryProperty.getPropertyWithFilter(query)
}