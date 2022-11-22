package com.example.oc_realestatemanager_p9.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.oc_realestatemanager_p9.BuildConfig
import com.example.oc_realestatemanager_p9.R
import com.example.oc_realestatemanager_p9.base.BaseFragment
import com.example.oc_realestatemanager_p9.models.Photo
import com.example.oc_realestatemanager_p9.models.Property
import com.example.oc_realestatemanager_p9.ui.activity.AddPropertyActivity
import com.example.oc_realestatemanager_p9.ui.adapter.DetailAdapter
import com.example.oc_realestatemanager_p9.utils.Utils
import com.example.oc_realestatemanager_p9.utils.Utils.buildTextAddress
import com.example.oc_realestatemanager_p9.viewmodel.Injection
import com.example.oc_realestatemanager_p9.viewmodel.PhotoListViewModel
import com.example.oc_realestatemanager_p9.viewmodel.PropertyListViewModel
import kotlinx.android.synthetic.main.fragment_detail_property.*
import timber.log.Timber


class DetailPropertyFragment: BaseFragment(), DetailAdapter.onClickItemListener {

    private lateinit var adapterDetail: DetailAdapter
    private lateinit var photoViewModel : PhotoListViewModel
    private lateinit var propertyViewModel : PropertyListViewModel
    private lateinit var photos : MutableList<Photo>
    private var propertyId : Long = 0

    companion object{
        const val PROPERTY_ID = "propertyId"
        var PROPERTY_ID_DETAIL  : Long = 0
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_detail_property
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        propertyId =  PROPERTY_ID_DETAIL
        Timber.d("property id is $propertyId")

        photos = mutableListOf()

        configureViewModel(propertyId)
        adapterDetail = DetailAdapter(photos, this)
        configureRecyclerView()

        button_update.setOnClickListener {
            val intent = Intent(requireContext(), AddPropertyActivity::class.java)
            intent.putExtra(PROPERTY_ID, propertyId)
            startActivity(intent)
            Timber.d("Click on update button")
        }
    }

    private fun configureViewModel(propertyId : Long){
        val factory = Injection.provideViewModelFactory()
        photoViewModel = ViewModelProvider(this, factory).get(PhotoListViewModel::class.java)
        photoViewModel.getPhotoToDisplay(propertyId).observe(viewLifecycleOwner, Observer { list -> updatePhotos(list) })

        propertyViewModel = ViewModelProvider(this, factory).get(PropertyListViewModel::class.java)
        propertyViewModel.getPropertyById(propertyId).observe(viewLifecycleOwner, Observer { property -> updateProperty(property) })
    }

    private fun updatePhotos(list : List<Photo>) {
        photos.clear()
        photos.addAll(list)
        adapterDetail.notifyDataSetChanged()
        Timber.d("list photo VM = ${photos.size}, $photos")
    }

    private fun configureRecyclerView() {
        val linear = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycler_detail.apply {
            layoutManager = linear
            itemAnimator = DefaultItemAnimator()
            adapter = adapterDetail
        }
    }

    private fun updateProperty(property: Property) {
        text_type.text = property.type
        text_price.text = resources.getString(R.string.devise_dollars, property.price.toString())
        text_description.text = property.description
        text_description.movementMethod = ScrollingMovementMethod()
        property_area.text = property.area.toString()
        property_address.text = buildTextAddress(property.street, property.town)
        property_room.text = property.roomNumber.toString()
        property_bathroom.text = property.bathroomNumber.toString()
        property_bedroom.text = property.bedroomNumber.toString()
        entry_date.text = property.entryDate
        if (property.hospital) poi_hospital.visibility = View.VISIBLE
        if (property.school) poi_school.visibility = View.VISIBLE
        if (property.market) poi_market.visibility = View.VISIBLE
        showCompromiseAndSoldText(property.compromiseDate, compromise_date, layout_compromise)
        showCompromiseAndSoldText(property.sellDate, sold_date, layout_sold)

        if (property.sellDate != ""){
            button_update.visibility = View.GONE
        }

        val streetText = Utils.makeStreetString(property.street,property.town)
        val uri = "https://maps.googleapis.com/maps/api/staticmap?zoom=15&size=200x200&maptype=roadmap&markers=color:red%7Clabel:P%7C"+
                streetText +"&center=" + streetText + "&key=" + BuildConfig.MAPS_API_KEY
        Glide.with(this)
            .load(uri)
            .apply(RequestOptions.centerCropTransform())
            .into(mini_Map)
    }


    private fun showCompromiseAndSoldText(text : String?, textView : TextView, layout : LinearLayout){
        if (text != null && text != ""){
            layout.visibility = View.VISIBLE
            textView.text = text
        }
    }

    /**
     * click on the image may do nothing in detail page
     */
    override fun onClickItem(image : Photo) {
    }

}
