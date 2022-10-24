package com.example.oc_realestatemanager_p9.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.oc_realestatemanager_p9.database.Database
import com.example.oc_realestatemanager_p9.provider.PropertyContentProvider
import com.example.oc_realestatemanager_p9.utils.ContentProviderTestData.generateProperty
import com.example.oc_realestatemanager_p9.utils.ContentProviderTestData.generateProperty2
import com.example.oc_realestatemanager_p9.utils.ContentProviderTestData.generateProperty3
import com.example.oc_realestatemanager_p9.utils.ContentProviderTestData.generatePropertyUpdated
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PropertyContentProviderTest {

    // FOR DATA
    private lateinit var mContentResolver: ContentResolver

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        Room.inMemoryDatabaseBuilder(context, Database::class.java)
            .allowMainThreadQueries()
            .build()
        mContentResolver = context.contentResolver
    }

    @Test
    fun getPropertyWhenNoPropertyWasInserted() {
        val cursor = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_PROPERTY, 500),
            null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(0))
        cursor.close()
    }

    @Test
    fun getPropertyDataBaseCountOnCreation() {
        val cursor = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_PROPERTY, 0), null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(0))
        cursor.close()
    }

    @Test
    fun insertAndGetProperty() {
        // BEFORE : Adding demo property from ContentProviderTestData
        val propertyUri: Uri = mContentResolver.insert(PropertyContentProvider.URI_PROPERTY, generateProperty())!!
        // TEST
        val cursor: Cursor = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_PROPERTY, 200),
                                        null, null, null, null)!!

        assertThat(propertyUri, notNullValue())
        assertThat(cursor.count, `is`(1))
        assertThat(cursor.moveToFirst(), `is`(true))
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("type")), `is`("Manor"))
        val uriDeleteProperty = mContentResolver.delete(propertyUri, null, null)//delete property
        cursor.close()
    }

    @Test
    fun updatePropertyWithProvider() {
        val itemUri = mContentResolver.insert(PropertyContentProvider.URI_PROPERTY, generateProperty2())
        assertThat(itemUri, notNullValue())

        val count = mContentResolver.update(itemUri!!, generatePropertyUpdated(), null, null)
        assertThat(count, `is`(1))

        val cursor = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_PROPERTY,300),
            null, null, null, null)

        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))
        assertThat(cursor.moveToFirst(), `is`(true))
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("type")), `is`("Penthouse"))
        val uriDeleteProperty = mContentResolver.delete(itemUri, null, null)//delete property
        cursor.close()
    }

    @Test
    fun deleteEstateWithProvider() {
        val itemUri = mContentResolver.insert(PropertyContentProvider.URI_PROPERTY, generateProperty3())
        assertThat(itemUri, notNullValue())

        val cursor1 = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_PROPERTY, 400),
            null, null, null, null)
        assertThat(cursor1, notNullValue())
        //assertThat(cursor1!!.count, `is`(1))
        cursor1?.close()

        val count = mContentResolver.delete(itemUri!!, null, null)
        assertThat(count, `is`(1))

        val cursor2 =  mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_PROPERTY, 400),
            null, null, null, null)

        assertThat(cursor2, notNullValue())
        assertThat(cursor2!!.count, `is`(0))
        cursor2.close()
    }
}