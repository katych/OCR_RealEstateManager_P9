package com.example.oc_realestatemanager_p9.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Color.RED
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.OnClick
import com.example.oc_realestatemanager_p9.R
import com.example.oc_realestatemanager_p9.app.App
import com.example.oc_realestatemanager_p9.base.BaseActivity
import com.example.oc_realestatemanager_p9.google_map.mapGeocodingDataToLatLong
import com.example.oc_realestatemanager_p9.models.EstateResult
import com.example.oc_realestatemanager_p9.models.Photo
import com.example.oc_realestatemanager_p9.models.Property
import com.example.oc_realestatemanager_p9.ui.adapter.DetailAdapter
import com.example.oc_realestatemanager_p9.ui.fragment.DetailPropertyFragment.Companion.PROPERTY_ID
import com.example.oc_realestatemanager_p9.utils.Utils.*
import com.example.oc_realestatemanager_p9.viewmodel.Injection
import com.example.oc_realestatemanager_p9.viewmodel.PhotoListViewModel
import com.example.oc_realestatemanager_p9.viewmodel.PropertyListViewModel
import com.google.android.gms.maps.model.LatLng
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_addproperty.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class AddPropertyActivity :  BaseActivity(), DetailAdapter.onClickItemListener {


    //FIELDS
    private lateinit var status: String
    private lateinit var street: String
    private lateinit var town: String
    private lateinit var description: String
    private lateinit var entryDate: String
    private lateinit var adapterDetail: DetailAdapter
    private lateinit var propertyListViewModel: PropertyListViewModel
    private lateinit var photoViewModel: PhotoListViewModel
    private lateinit var uri: Uri
    private lateinit var image: Photo
    private lateinit var notificationManager: NotificationManagerCompat

    private var type: String = ""
    private var area: Int = 0
    private var rooms: Int = 0
    private var price: Double = 0.0
    private var priceEuro: Double = 0.0
    private var bedrooms: Int = 0
    private var bathrooms: Int = 0
    private var imageDescription: String = ""
    private var propertyId: Long = 0
    private var photosListDetail: MutableList<Photo> = mutableListOf()
    private var photos: MutableList<Photo> = mutableListOf()
    private var compromiseDate: String = ""
    private var soldDate: String = ""
    private var hospital: Boolean = false
    private var school: Boolean = false
    private var market: Boolean = false
    private var complete: Boolean = true
    private var estateLat: Double = 0.0
    private var estateLong: Double = 0.0
    private var entryDateLong: Long = 0
    private var soldDateLong: Long = 0
    private var isUpdated : Boolean = false

    companion object {
        private const val RESULT_CAMERA_CODE = 20
        private const val RESULT_GALLERY_CODE = 30
    }

    override fun getActivityLayout(): Int {
        return R.layout.activity_addproperty
    }

    override fun getToolbar(): Toolbar? {
        return toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.configureToolBar("Add property")

        notificationManager = NotificationManagerCompat.from(this)

        propertyId = intent.getLongExtra(PROPERTY_ID, 0)
        Timber.d("Property add = $propertyId")

        adapterDetail = DetailAdapter(photos, this)

        configureSpinner()
        configureRecyclerView()
        date_on_sale.text = getTodayDate()

        val factory = Injection.provideViewModelFactory()

        propertyListViewModel = ViewModelProvider(this, factory).get(PropertyListViewModel::class.java)
        photoViewModel = ViewModelProvider(this, factory).get(PhotoListViewModel::class.java)

        spinner_type.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                val textSpinner = spinner_type.selectedItem.toString()
                type = textSpinner
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        if (propertyId > 0) {
            this.configureToolBar("Update property")

            isUpdated = true
            photoViewModel.getPhotoToDisplay(propertyId)
                .observe(this, Observer { photoList -> updateAdapter(photoList) })
        }

    }

    private fun configureSpinner() {
        val adapterSpinner: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.type,
            android.R.layout.simple_spinner_item
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_type.adapter = adapterSpinner
    }


    private fun updateAdapter(photoList: List<Photo>) {
        photosListDetail.clear()
        photosListDetail.addAll(photoList)
        propertyListViewModel.getPropertyById(propertyId)
            .observe(this, Observer { property -> updateUi(property) })
    }

    private fun getPositionSpinner(spinner : Spinner, myString : String){
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == myString) {
                spinner.setSelection(i)
                break
            }
        }
    }

    private fun updateUi(property: Property) {
        getPositionSpinner(spinner_type, property.type)
        card_street.setText(property.street)
        card_town.setText(property.town)
        card_description.setText(property.description)
        text_area.setText(property.area.toString())
        card_rooms.setText(property.roomNumber.toString())
        card_bedroom.setText(property.bedroomNumber.toString())
        card_bathroom.setText(property.bathroomNumber.toString())
        card_price.setText(property.price.toString())
        date_on_sale.text = property.entryDate
        layout_compromise_add.visibility = View.VISIBLE
        if (property.compromiseDate != "") {
            check_compromise.isChecked = true
            layout_sold_add.visibility = View.VISIBLE
            check_compromise.isClickable = false
            date_compromise.text = property.compromiseDate
        }
        if (property.hospital) {
            chip_hospital.isChecked = true
            stateChip(chip_hospital, this.applicationContext)
        }
        if (property.school) {
            chip_school.isChecked = true
            stateChip(chip_school, this.applicationContext)
        }
        if (property.market) {
            chip_market.isChecked = true
            stateChip(chip_market, this.applicationContext)
        }
        fab.visibility = View.GONE

        fab_update.visibility = View.VISIBLE
        photos.clear()
        photos.addAll(photosListDetail)
        adapterDetail.notifyDataSetChanged()
    }

    @OnClick(
        R.id.fab, R.id.camera, R.id.gallery, R.id.fab_update, R.id.check_compromise,
        R.id.check_sold, R.id.chip_hospital, R.id.chip_market, R.id.chip_school
    )
    fun onClickBottomNavigation(view: View) {
        when (view.id) {
            R.id.fab, R.id.fab_update -> checkPropertyInformation()
            R.id.camera -> checkPermissionsCamera()
            R.id.gallery -> checkPermissionsGallery()
            R.id.check_compromise -> if (date_compromise.text == "") date_compromise.text = getTodayDate() else date_compromise.text = ""
            R.id.check_sold -> if (date_sold.text == "") date_sold.text = getTodayDate() else date_sold.text = ""
            R.id.chip_hospital -> stateChip(chip_hospital, this.applicationContext)
            R.id.chip_market -> stateChip(chip_market, this.applicationContext)
            R.id.chip_school -> stateChip(chip_school, this.applicationContext)
        }
    }

    private fun configureRecyclerView() {
        val linear = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_image.apply {
            layoutManager = linear
            itemAnimator = DefaultItemAnimator()
            adapter = adapterDetail

        }
    }

    private fun checkPropertyInformation() {

        if ( !checkEditTextInput(card_description.text.toString()) || !checkEditTextInput(text_area.text.toString()) || !checkEditTextInput(card_rooms.text.toString())
            || !checkEditTextInput(card_bedroom.text.toString()) || !checkEditTextInput(card_bathroom.text.toString()) || !checkEditTextInput(card_price.text.toString())
        ) {
            complete = false
        }

        if (card_town.text.toString() == "" || card_street.text.toString() == ""){
            makeSnackBar(constraint_add, "You must have to enter a town and a street")
            card_town.setBackgroundColor(resources.getColor(R.color.background_false))
            card_town.setTextColor(resources.getColor(R.color.icons))
            card_street.setBackgroundColor(resources.getColor(R.color.background_false))
            card_street.setTextColor(resources.getColor(R.color.icons))
            return
        }

        street = card_street.text.toString()
        town = card_town.text.toString()
        description = card_description.text.toString()
        area = if (text_area.text.toString() == "") 0 else Integer.parseInt(text_area.text.toString())
        rooms = if (card_rooms.text.toString() == "") 0 else Integer.parseInt(card_rooms.text.toString())
        bedrooms = if (card_bedroom.text.toString() == "") 0 else Integer.parseInt(card_bedroom.text.toString())
        bathrooms = if (card_bathroom.text.toString() == "") 0 else Integer.parseInt(card_bathroom.text.toString())
        price = if (card_price.text.toString() == "") 0.0 else card_price.text.toString().toDouble()
        if (chip_hospital.isChecked) hospital = true
        if (chip_school.isChecked) school = true
        if (chip_market.isChecked) market = true
        entryDate = date_on_sale.text.toString()
        entryDateLong = convertDateToLong(entryDate)
        compromiseDate = date_compromise.text.toString()
        soldDate = date_sold.text.toString()
        if (soldDate != ""){
            soldDateLong = System.currentTimeMillis()
        }

        status = getStatus()

        getCoordinate()
        if (!isUpdated){
            notificationCreated()
        }
    }

    private fun getCoordinate() {
        val address = makeStreetString(street, town)
        val estateResult = App.addressCoordonateApiService.getCoordinateFromAddress(address)
        estateResult.enqueue(object : Callback<EstateResult> {
            override fun onResponse(call: Call<EstateResult>, response: Response<EstateResult>) {

                response.body()?.let {
                    val latLng = mapGeocodingDataToLatLong(it)
                    getLatitudeAndLongitude(latLng)
                    Timber.d("estate result LatLng = $estateLat et $estateLong")
                    if (propertyId > 0) {
                        updateBddWithNewInformation()
                    } else {
                        createPropertyInBdd()
                    }
                }
            }

            override fun onFailure(call: Call<EstateResult>, t: Throwable) {
                Timber.d("Not yet implemented")
            }
        })
    }

    private fun getLatitudeAndLongitude(latLng: LatLng) {
        estateLat = latLng.latitude
        estateLong = latLng.longitude
    }

    private fun getStatus(): String {
        status = if(compromiseDate != "" && soldDate == ""){
            "Compromise signed"
        }else if(soldDate != ""){
            "Sold"
        }else{
            "On sale"
        }
        return status
    }


    private fun createPropertyInBdd() {
        if (photos.size == 0){
            val photo = Photo(0, "https://upload.wikimedia.org/wikipedia/commons/a/ac/No_image_available.svg", "No Photo", 0)
            photos.add(photo)
        }
        val property = Property(0, type, price, priceEuro, area, rooms, bedrooms, bathrooms, description, photos[0].urlPhoto,
            photos.size, street, town, estateLat, estateLong, hospital, school, market, status, entryDate, entryDateLong,"", "", soldDateLong, 1, complete
        )

        propertyListViewModel.createProperty(property, photos)
        photoViewModel.insertPhotos(photos)
        finish()
    }

    private fun updateBddWithNewInformation() {
        Timber.d("estate LatLng = $estateLat et $estateLong")
        if (photos.size == 0){
            val photo = Photo(0,"https://upload.wikimedia.org/wikipedia/commons/0/06/No_Photo_Available_Placeholder_Image.png", "No Photo", 0)
            photos.add(photo)
        }
        val property = Property(propertyId, type, price, priceEuro, area, rooms, bedrooms, bathrooms, description, photos[0].urlPhoto, photos.size, street,
            town, estateLat, estateLong, hospital, school, market, status, entryDate, entryDateLong, compromiseDate, soldDate, soldDateLong,1, complete
        )

        Timber.d("photoListDetail = ${photosListDetail.size}, $photosListDetail")
        Timber.d("photos = ${photos.size}, $photos")
        propertyListViewModel.updateProperty(property, photos)

        photoViewModel.deletePhotos(photosListDetail)
        photoViewModel.insertPhotos(photos)
        finish()
    }

    private fun openGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, RESULT_GALLERY_CODE)
        }

    }

    private fun openCamera() {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "New Picture")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "From the camera")
        uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!

        //Intent for camera
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, RESULT_CAMERA_CODE)
    }


    // ***************************
    // PERMISSIONS
    // ***************************

    private fun checkPermissionsGallery() {
        Dexter
            .withActivity(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    openGallery()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    Timber.d("Permission Rationale")
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Timber.d("Permission Denied")
                }
            })
            .check()
    }

    // Check multiples permissions
    private fun checkPermissionsCamera() {
        Dexter
            .withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    openCamera()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    Timber.d("Permission Rationale")
                }
            })
            .check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RESULT_CAMERA_CODE -> showDescriptionDialog(uri.toString())
            RESULT_GALLERY_CODE -> showDescriptionDialog(data?.data.toString())
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * show a dialog to enter photo description if we click on camera or gallery button
     */
    private fun showDescriptionDialog(path: String) {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.photo_name))

        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.description_layout, viewGroup, false)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.description_field)

        builder.setView(dialogView)
        builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
            imageDescription = descriptionEditText.text.toString()
            Timber.d("Uri image = $path")

            image = Photo(0, path, imageDescription, 0)
            photos.add(image)
            adapterDetail.notifyDataSetChanged()
        }
        builder.show()
    }

    /**
     * create dialog to delete image
     */
    private fun showDeleteImageDialog(image: Photo) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.delete_message))

        builder.setPositiveButton(getString(R.string.ok)) { _, _ ->

            photos.remove(image)
            adapterDetail.notifyDataSetChanged()
        }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->

            }
        builder.show()
    }

    /**
     * create notification when estate is created
     */
    private fun notificationCreated() {
        val title: String = getString(R.string.app_name)
        val contentText = "Property $type has been well created"

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.new_york_night)

        val builder = NotificationCompat.Builder(this, App.channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(contentText)
            .setLargeIcon(bitmap)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .build()
        notificationManager.notify(1, builder)
    }

    override fun onClickItem(image: Photo) {
        showDeleteImageDialog(image)
    }

}
