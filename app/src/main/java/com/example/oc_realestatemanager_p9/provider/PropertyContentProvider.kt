package com.example.oc_realestatemanager_p9.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.example.oc_realestatemanager_p9.app.App
import com.example.oc_realestatemanager_p9.models.Property

class PropertyContentProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = "com.example.oc_realestatemanager_p9.provider"
        private val TABLE_NAME = Property::class.java.simpleName
        val URI_PROPERTY = Uri.parse("content://$AUTHORITY/$TABLE_NAME")!!

    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {

        if (context != null) {
            val userId = ContentUris.parseId(uri)
            val cursor: Cursor = App.db.propertyDao().getPropertyByIdWithCursor(userId)
            cursor.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        }

        throw java.lang.IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (context != null){
            val id  : Long = App.db.propertyDao().createProperty(Property.fromContentValues(values!!))

            if (id > 0){
                context!!.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
        }

        throw java.lang.IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        if (context != null){
            val property = Property.fromContentValues(values!!)
            property.id = ContentUris.parseId(uri)
            val count = App.db.propertyDao().updateProperty(property)

            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw java.lang.IllegalArgumentException("Failed to insert row into $uri")
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        if (context != null){
            val count = App.db.propertyDao().deletePropertyById(ContentUris.parseId(uri))

            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw java.lang.IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

}