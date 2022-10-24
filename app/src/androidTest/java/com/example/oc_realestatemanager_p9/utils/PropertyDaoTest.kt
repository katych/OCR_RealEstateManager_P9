package com.example.oc_realestatemanager_p9.utils

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.oc_realestatemanager_p9.database.Database
import com.example.oc_realestatemanager_p9.models.Property
import com.example.oc_realestatemanager_p9.R


import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PropertyDaoTest {

    //For Data
    private lateinit var db : Database


    private val property1 = Property(1,"Manoir", 1200000.0, 0.0,250, 6, 4,1,"New dream mansion by the water with a serene view of the bay and the Indian Creek golf course! Inside, a spectacular contemporary design including open and open living spaces surrounded by oversized glass walls, an interior garden, a formal dining room, a chef's kitchen + a 2nd full kitchen, a large upper living room, a sauna and high-end finishes. Exceptional exterior with an infinity pool, spa, summer kitchen and a ready roof - ideal for entertaining and dolphin watching. Deluxe Master offers an ultra-lux marble bathtub, a terrace, a dressing room and endless sunsets! Equipped with Lutron lighting and blinds, 2 car garage with elevator, lush landscape. Live in the exclusive islands of Bay Harbor, minutes from Bal Harbor!",
        R.drawable.manoir_de_dubourvieux.toString(),2, "lumee", "Manama",26.22009,50.60419,false, true, false, "On sale", "24/02/2020", 1582498800000,"", "", 1,1,true)
    private val property2 = Property(2,"Manoir", 1200000.0, 0.0, 250, 6, 4,1,"New dream mansion by the water with a serene view of the bay and the Indian Creek golf course! Inside, a spectacular contemporary design including open and open living spaces surrounded by oversized glass walls, an interior garden, a formal dining room, a chef's kitchen + a 2nd full kitchen, a large upper living room, a sauna and high-end finishes. Exceptional exterior with an infinity pool, spa, summer kitchen and a ready roof - ideal for entertaining and dolphin watching. Deluxe Master offers an ultra-lux marble bathtub, a terrace, a dressing room and endless sunsets! Equipped with Lutron lighting and blinds, 2 car garage with elevator, lush landscape. Live in the exclusive islands of Bay Harbor, minutes from Bal Harbor!",
        R.drawable.manoir_de_dubourvieux.toString(), 1,"21 jump street ", "New York", 0.0,0.0,false, true, false, "On sale", "24/02/2020", 1582498800000, "","", 1,2,true)
    private val properties = listOf(property1, property2)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        this.db = Room.inMemoryDatabaseBuilder(context, Database::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        db.close()
    }



    @Test
    @Throws(InterruptedException::class)
    fun insertProperty_WithSuccess() {
        // BEFORE : Adding properties
        this.db.propertyDao().createProperty(property1)
        this.db.propertyDao().createProperty(property2)

        // TEST
        val properties : List<Property> =  LiveDataTestUtils.getValue(
            this.db.propertyDao().getAllProperties()
        )
        assertTrue(properties.size == 2)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getProperty_WithSuccess() {
        // BEFORE : Adding properties
        this.db.propertyDao().insertProperties(properties)
        // TEST
        val property : Property =  LiveDataTestUtils.getValue(
            this.db.propertyDao().getProperty(1))
        val property2 : Property =  LiveDataTestUtils.getValue(
            this.db.propertyDao().getProperty(2))
        assertEquals(this.property1.town, property.town)
        assertEquals(this.property2.type, property2.type)
    }

    @Test
    @Throws(InterruptedException::class)
    fun updateProperty_WithSuccess(){
        // BEFORE : Adding properties list
        this.db.propertyDao().insertProperties(properties)
        //Get property 1
        val property1 = LiveDataTestUtils.getValue(this.db.propertyDao().getProperty(1))
        //Update type of property
        val propertyUpdate = property1.copy(type = "Loft")
        // update property in BDD
        this.db.propertyDao().updateProperty(propertyUpdate)
        //Get property updated
        val propertyUpdated = LiveDataTestUtils.getValue(this.db.propertyDao().getProperty(1))

        assertNotSame(property1.type, propertyUpdated.type)
        assertEquals("Loft", propertyUpdated.type)
    }


    @Test
    @Throws(InterruptedException::class)
    fun deleteProperty_WithSuccess(){
        // BEFORE : Adding properties list
        this.db.propertyDao().insertProperties(properties)

        // delete property with id 1
        this.db.propertyDao().deleteProperty(property1)
        // get all properties
        val propertiesAfterDelete = LiveDataTestUtils.getValue(this.db.propertyDao().getAllProperties())

       assertTrue(propertiesAfterDelete.size == 1)
    }
}