package com.example.oc_realestatemanager_p9.models

import android.content.ContentValues
import androidx.room.*
import androidx.room.ForeignKey.CASCADE


@Entity(tableName = "estate")
data class Property(
            @PrimaryKey(autoGenerate = true)
            var id: Long,
            var type: String,
            var price: Double,
            var priceEuro: Double,
            var area: Int,
            @ColumnInfo(name = "room_number")
            var roomNumber: Int,
            var bedroomNumber: Int,
            var bathroomNumber: Int,
            var description: String,
            var photo: String,
            var numberPhotos : Int,
            var street : String,
            var town : String,
            @ColumnInfo(name = "estate_lat")
            var estateLat : Double? = null,
            @ColumnInfo(name = "estate_long")
            var estateLong : Double? = null,
            var hospital: Boolean = false,
            var school: Boolean = false,
            var market: Boolean = false,
            var status: String,
            @ColumnInfo(name = "entry_date")
            var entryDate: String,
            @ColumnInfo(name = "entry_date_long")
            var entryDateLong: Long,
            @ColumnInfo(name = "compromise_date")
            var compromiseDate: String,
            @ColumnInfo(name = "sell_date")
            var sellDate: String,
            @ColumnInfo(name = "sell_date_long")
            var sellDateLong: Long,
            @ColumnInfo(name = "user_id")
            var userId: Int,
            var complete : Boolean = false
) {

    companion object{

        fun fromContentValues(values: ContentValues) : Property {
            val property = Property(0, "",0.0,0.0,0,0,0,0,"",
                "",0, "", "", null, null, hospital = false, school = false, market = false, status = "",
                entryDate = "", entryDateLong = 0,compromiseDate = "", sellDate = "",sellDateLong = 0, userId = 0)

            if (values.containsKey("id")) property.id = values.getAsLong("id")
            if (values.containsKey("type")) property.type = values.getAsString("type")
            if (values.containsKey("price")) property.price = values.getAsDouble("price")
            if (values.containsKey("priceEuro")) property.price = values.getAsDouble("priceEuro")
            if (values.containsKey("area")) property.area = values.getAsInteger("area")
            if (values.containsKey("roomNumber")) property.roomNumber = values.getAsInteger("roomNumber")
            if (values.containsKey("bedroomNumber")) property.bedroomNumber = values.getAsInteger("bedroomNumber")
            if (values.containsKey("bathroomNumber")) property.bathroomNumber = values.getAsInteger("bathroomNumber")
            if (values.containsKey("description")) property.description = values.getAsString("description")
            if (values.containsKey("photo")) property.photo = values.getAsString("photo")
            if (values.containsKey("numberPhotos")) property.numberPhotos = values.getAsInteger("numberPhotos")
            if (values.containsKey("street")) property.street = values.getAsString("street")
            if (values.containsKey("town")) property.town = values.getAsString("town")
            if (values.containsKey("estateLat")) property.estateLat = values.getAsDouble("estateLat")
            if (values.containsKey("estateLong")) property.estateLong = values.getAsDouble("estateLong")
            if (values.containsKey("hospital")) property.hospital = values.getAsBoolean("hospital")
            if (values.containsKey("school")) property.school = values.getAsBoolean("school")
            if (values.containsKey("market")) property.market = values.getAsBoolean("market")
            if (values.containsKey("status")) property.status = values.getAsString("status")
            if (values.containsKey("entryDate")) property.entryDate = values.getAsString("entryDate")
            if (values.containsKey("entryDateLong")) property.entryDateLong = values.getAsLong("entryDateLong")
            if (values.containsKey("compromiseDate")) property.compromiseDate = values.getAsString("compromiseDate")
            if (values.containsKey("sellDate")) property.sellDate = values.getAsString("sellDate")
            if (values.containsKey("sellDateLong")) property.sellDateLong = values.getAsLong("sellDateLong")
            if (values.containsKey("userId")) property.userId = values.getAsInteger("userId")
            if (values.containsKey("complete")) property.complete = values.getAsBoolean("complete")

            return property
        }
    }

}


@Entity(
    tableName = "photo",
    foreignKeys = [ForeignKey(
        entity = Property::class,
        parentColumns = ["id"],
        childColumns = ["property_id"],
        onDelete = CASCADE
    )],
    indices = [Index(value = ["property_id"])]
)


data class Photo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_photo")
    var idPhoto: Int,
    @ColumnInfo(name = "url_photo")
    var urlPhoto: String,
    var name: String,
    @ColumnInfo(name = "property_id")
    var propertyId: Long
)

data class Poi(
    var name : String,
    var street : String,
    var photo : String,
    var lat : Double,
    var long : Double,
    var estateId : Long
)