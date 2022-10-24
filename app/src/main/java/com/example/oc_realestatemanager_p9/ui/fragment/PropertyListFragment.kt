package com.example.oc_realestatemanager_p9.ui.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.oc_realestatemanager_p9.R
import com.example.oc_realestatemanager_p9.app.App.Companion.appContext
import com.example.oc_realestatemanager_p9.app.App.Companion.db
import com.example.oc_realestatemanager_p9.base.BaseFragment
import com.example.oc_realestatemanager_p9.database.Database
import com.example.oc_realestatemanager_p9.models.Property
import com.example.oc_realestatemanager_p9.ui.activity.AddPropertyActivity
import com.example.oc_realestatemanager_p9.ui.adapter.PropertyListAdapter
import com.example.oc_realestatemanager_p9.utils.Utils.*
import com.example.oc_realestatemanager_p9.viewmodel.Injection
import com.example.oc_realestatemanager_p9.viewmodel.PropertyListViewModel
import kotlinx.android.synthetic.main.filter_query_dialog.view.*
import kotlinx.android.synthetic.main.fragment_property_list.*
import timber.log.Timber
import java.util.*

class PropertyListFragment : BaseFragment() , PropertyListAdapter.PropertyListAdapterListener {

    interface OnClickEstateListener{
        fun onClickItemEstate(propertyId : Long)
    }

    private lateinit var viewModel: PropertyListViewModel
    private lateinit var propertyListAdapter: PropertyListAdapter
    private var properties: MutableList<Property> = mutableListOf()
    private var town : String = ""
    private var minPrice : Double = 0.0
    private var maxPrice : Double = 0.0
    private var minRoom : Int= 0
    private var maxRoom : Int = 0
    private var minSurface : Int = 0
    private var maxSurface : Int = 0
    private var numberPhotos : Int= 0
    private var entryDateLong : Long = 0
    private var soldDateLong : Long = 0
    private var sold : String = ""
    private var entryDate: String = ""
    private var sellDate: String = ""
    private var hospital: Boolean = false
    private var school : Boolean = false
    private var market: Boolean = false
    private var listener : OnClickEstateListener? = null
    private var mDateSetListener: OnDateSetListener? = null
    private var mDateSetListener2: OnDateSetListener? = null


    companion object {
        internal var DEVISE : String = "dollars"
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_property_list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            // finish activity if we press back button only on this fragment
            requireActivity().finishAffinity()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClickEstateListener){
            listener = context
        }else{
            throw RuntimeException("$context must implemente PropertyListFragment interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

       val factory = Injection.provideViewModelFactory()
        viewModel = ViewModelProvider(this,factory).get(PropertyListViewModel::class.java)
        viewModel.properties.observe(viewLifecycleOwner, Observer { newProperty -> updateProperty(newProperty) })

        propertyListAdapter = PropertyListAdapter(properties, this)

        recycler_property.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = propertyListAdapter
        }

        fab_add_property.setOnClickListener {
            val intent = Intent(this.requireContext(), AddPropertyActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_money -> {
                if (DEVISE == "dollars") {
                    for (p in properties) {
                        p.priceEuro = convertDollarToEuro(p.price.toInt()).toDouble()
                    }
                    DEVISE = "euro"
                    propertyListAdapter.notifyDataSetChanged()
                } else {
                    DEVISE = "dollars"
                    propertyListAdapter.notifyDataSetChanged()
                }
            }
            R.id.filter -> alertDialogQuery()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun alertDialogQuery() {

        val viewGroup = activity?.findViewById<ViewGroup>(android.R.id.content)
        //Inflate dialog with custom layout
        val mDialog = LayoutInflater.from(activity).inflate(R.layout.filter_query_dialog, viewGroup, false)
        //build the dialog with custom view
        val mBuilder = AlertDialog.Builder(activity).setView(mDialog)
        //show dialog
        val mAlertDialog = mBuilder.show()

        mDialog.entry_filter.setOnClickListener { configureDialogCalendar(mDateSetListener!!) }
        mDialog.sold_filter.setOnClickListener { configureDialogCalendar(mDateSetListener2!!) }

        mDateSetListener = OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
            var day = dayOfMonth.toString()
            var monthS = (month + 1).toString()
            if (dayOfMonth < 10) {
                day = "0$dayOfMonth"
            }
            if (month < 9) {
                monthS = "0" + (month + 1)
            }
            entryDate = "$day/$monthS/$year"
            mDialog.entry_date_filter.text = entryDate
        }

        mDateSetListener2 = OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
            var day = dayOfMonth.toString()
            var monthS = (month + 1).toString()
            if (dayOfMonth < 10) {
                day = "0$dayOfMonth"
            }
            if (month < 9) {
                monthS = "0" + (month + 1)
            }
            sellDate = "$day/$monthS/$year"
            mDialog.sold_date_filter.text = sellDate
        }

        mDialog.button_filter.setOnClickListener {
            val listType = ArrayList<String>()

            if (mDialog.radio_Manor.isChecked) listType.add(mDialog.radio_Manor.text.toString())
            if (mDialog.radio_Penthouse.isChecked) listType.add(mDialog.radio_Penthouse.text.toString())
            if (mDialog.radio_Loft.isChecked) listType.add( mDialog.radio_Loft.text.toString())
            if (mDialog.radio_house.isChecked) listType.add(mDialog.radio_house.text.toString())

            town = mDialog.filter_town.text.toString()
            minPrice = checkData(mDialog.filter_min_price.text.toString()).toDouble()
            maxPrice = checkMaxData(mDialog.filter_max_price.text.toString())
            minRoom = checkData(mDialog.filter_min_room.text.toString())
            maxRoom = checkMaxData(mDialog.filter_max_room.text.toString()).toInt()
            minSurface = checkData(mDialog.filter_min_area.text.toString())
            maxSurface = checkMaxData(mDialog.filter_max_area.text.toString()).toInt()
            numberPhotos = checkMaxData(mDialog.filter_number_photos.text.toString()).toInt()

            when {
                mDialog.radio_on_sale.isChecked -> sold = mDialog.radio_on_sale.text.toString()
                mDialog.radio_sold.isChecked -> sold = mDialog.radio_sold.text.toString()
            }

            entryDateLong = convertDateToLong(entryDate)
            soldDateLong = convertDateToLong(sellDate)

            hospital = mDialog.radio_hospital.isChecked
            school  = mDialog.radio_school.isChecked
            market = mDialog.radio_Market.isChecked

            Timber.d("Parameters : $listType, $town, $minPrice, $maxPrice, $minRoom, $maxRoom, $minSurface, $maxSurface, $numberPhotos, $sold, $entryDate, $entryDateLong, $sellDate, $soldDateLong, $hospital,$school, $market ")

            refreshListProperty(listType)
            mAlertDialog.dismiss()
        }

        mDialog.button_Nofilter.setOnClickListener {
            viewModel.properties.observe(viewLifecycleOwner, Observer { estate -> updateProperty(estate) })
            mAlertDialog.dismiss()
        }
    }


    private fun updateProperty(newProperty: List<Property>) {
        Timber.d("List property in update = ${newProperty.size}")
        properties.clear()
        properties.addAll(newProperty)
        Timber.d("List of property = ${properties.size},  $properties")
        checkIfNoProperty()
        propertyListAdapter.notifyDataSetChanged()
    }

    private fun refreshListProperty(listType : List<String>) {
        Timber.d("List de biens = $listType")
        // List of bind parameters
        val args: ArrayList<Any> = arrayListOf(minPrice, maxPrice, minRoom, maxRoom, minSurface, maxSurface, numberPhotos, sold, entryDateLong, soldDateLong)

        var query  : String = """SELECT * FROM estate WHERE price BETWEEN ? AND ? 
            |AND room_number BETWEEN ? AND ? AND area BETWEEN ? AND ? 
            |AND numberPhotos BETWEEN 0 AND ? AND status = ? AND entry_date_long >= ? AND sell_date_long >= ?""".trimMargin()

        if (town != ""){
            query += " AND"
            query += " town LIKE ?"
            args.add(town)
        }
        if (hospital){
            query += " AND"
            query += " hospital = ?"
            args.add(hospital)
            Timber.d("hospital = $hospital")
        }
        if (school){
            query += " AND"
            query += " school = ?"
            args.add(school)
            Timber.d("school = $school")
        }
        if (market){
            query += " AND"
            query += " market = ?"
            args.add(market)
        }
        if (listType.isNotEmpty()){
            for (i in listType.indices){
                if (i == 0){
                    query += " AND"
                    query += " type = ?"
                    args.add(listType[i])
                }else{
                    query += " OR"
                    query += " type = ?"
                    args.add(listType[i])
                }
            }
        }

        Timber.d("query = $query")
        val queryEntry = SimpleSQLiteQuery(query, args.toTypedArray())
        viewModel.getPropertyWithFilter(queryEntry).observe(viewLifecycleOwner, Observer { property -> updateProperty(property) })
    }



    private fun checkIfNoProperty() {
        if (propertyListAdapter.itemCount == 0) {
            textViewNoProperty.visibility = View.VISIBLE
        } else textViewNoProperty.visibility = View.GONE
    }

    private fun configureDialogCalendar(dateListener : OnDateSetListener) {
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        val dialogDate = DatePickerDialog(
            requireContext(), dateListener,
            year, month, day
        )
        dialogDate.show()
    }

    override fun onPropertySelected(property: Property) {
        listener?.onClickItemEstate(property.id)
    }

}






