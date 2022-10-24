package com.example.oc_realestatemanager_p9.utils

import android.content.ContentValues

object ContentProviderTestData {

    fun generateProperty(): ContentValues {
        val values = ContentValues()
        values.put("id", 200)
        values.put("type", "Manor")
        values.put("price", 3000000.0)
        values.put("area", 250)
        values.put("roomNumber", 9)
        values.put("bedroomNumber", 5)
        values.put("bathroomNumber", 2)
        values.put("description", "good")
        values.put("photo", "")
        values.put("numberPhotos", 0)
        values.put("street", "46 rue labas")
        values.put("town", "New York")
        values.put("hospital", true)
        values.put("school", true)
        values.put("market", true)
        values.put("status", "on Sale")
        values.put("entryDate", "24/02/2020")
        values.put("compromiseDate", "")
        values.put("sellDate", "")
        values.put("userId", 1)
        values.put("complete", true)
        return values
    }


    fun generateProperty2(): ContentValues {
        val values = ContentValues()
        values.put("id", 300)
        values.put("type", "Loft")
        values.put("price", 2500000.0)
        values.put("area", 350)
        values.put("roomNumber", 10)
        values.put("bedroomNumber", 7)
        values.put("bathroomNumber", 2)
        values.put("description", "good")
        values.put("photo", "")
        values.put("numberPhoto", 0)
        values.put("street", "4 avenue du paradis")
        values.put("town", "New York")
        values.put("hospital", true)
        values.put("school", false)
        values.put("market", true)
        values.put("status", "on Sale")
        values.put("entryDate", "24/02/2020")
        values.put("compromiseDate", "")
        values.put("sellDate", "")
        values.put("userId", 1)
        values.put("complete", true)
        return values
    }

    fun generateProperty3(): ContentValues {
        val values = ContentValues()
        values.put("id", 400)
        values.put("type", "Penthouse")
        values.put("price", 4000000.0)
        values.put("area", 350)
        values.put("roomNumber", 10)
        values.put("bedroomNumber", 7)
        values.put("bathroomNumber", 2)
        values.put("description", "good")
        values.put("photo", "")
        values.put("numberPhoto", 0)
        values.put("street", "4 avenue du paradis")
        values.put("town", "Miami")
        values.put("hospital", true)
        values.put("school", false)
        values.put("market", true)
        values.put("status", "on Sale")
        values.put("entryDate", "24/02/2020")
        values.put("compromiseDate", "")
        values.put("sellDate", "")
        values.put("userId", 1)
        values.put("complete", true)
        return values
    }

    fun generatePropertyUpdated(): ContentValues {
        val values = ContentValues()
        values.put("id", 300)
        values.put("type", "Penthouse")
        values.put("price", 2500000.0)
        values.put("area", 350)
        values.put("roomNumber", 10)
        values.put("bedroomNumber", 7)
        values.put("bathroomNumber", 2)
        values.put("description", "good")
        values.put("photo", "")
        values.put("numberPhoto", 0)
        values.put("street", "4 avenue du paradis")
        values.put("town", "NewYork")
        values.put("hospital", true)
        values.put("school", false)
        values.put("market", true)
        values.put("status", "on Sale")
        values.put("entryDate", "24/02/2020")
        values.put("compromiseDate", "")
        values.put("sellDate", "")
        values.put("userId", 1)
        values.put("complete", true)
        return values
    }
}