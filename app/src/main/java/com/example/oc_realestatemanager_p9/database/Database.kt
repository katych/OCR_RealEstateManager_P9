package com.example.oc_realestatemanager_p9.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.oc_realestatemanager_p9.database.dao.PhotoDao
import com.example.oc_realestatemanager_p9.database.dao.PropertyDao
import com.example.oc_realestatemanager_p9.models.Photo
import com.example.oc_realestatemanager_p9.models.Property


const val DATABASE_NAME = "real_estate"

@Database(entities = [Property::class, Photo::class],
    version = 1, exportSchema = false)

abstract class Database : RoomDatabase() {

    //DAO
    abstract fun propertyDao() : PropertyDao
    abstract fun photoDao() : PhotoDao


    companion object{
        @Volatile
        private var INSTANCE : com.example.oc_realestatemanager_p9.database.Database? = null

        fun getDatabase(context : Context) :com.example.oc_realestatemanager_p9.database.Database {
            //if the instance is not null return it
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    com.example.oc_realestatemanager_p9.database.Database::class.java,
                    DATABASE_NAME
                )
                    .addCallback(FakePropertyApi.prepopulateDatabase())
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}