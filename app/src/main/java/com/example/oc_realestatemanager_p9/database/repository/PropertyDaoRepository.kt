package com.example.oc_realestatemanager_p9.database.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.oc_realestatemanager_p9.database.dao.PropertyDao
import com.example.oc_realestatemanager_p9.models.Property

class PropertyDaoRepository(private val propertyDao : PropertyDao) {

    var long : Long = 0
    val properties = propertyDao.getAllProperties()

    fun getPropertyById(propertyId : Long) : LiveData<Property> = propertyDao.getProperty(propertyId)

    fun createProperty(property : Property) : Long {
        long = propertyDao.createProperty(property)
        return long
    }

    fun updateProperty(property: Property) {
        propertyDao.updateProperty(property)
    }

    fun getPropertyWithFilter(query: SupportSQLiteQuery) : LiveData<List<Property>> = propertyDao.getPropertyWithFilter(query)
}