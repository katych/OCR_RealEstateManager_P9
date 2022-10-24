package com.example.oc_realestatemanager_p9.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.bumptech.glide.Glide
import com.example.oc_realestatemanager_p9.R
import com.example.oc_realestatemanager_p9.base.BaseActivity
import com.example.oc_realestatemanager_p9.firebase.UsersHelpers
import com.example.oc_realestatemanager_p9.ui.fragment.DetailPropertyFragment
import com.example.oc_realestatemanager_p9.ui.fragment.DetailPropertyFragment.Companion.PROPERTY_ID_DETAIL
import com.example.oc_realestatemanager_p9.ui.fragment.MapFragment
import com.example.oc_realestatemanager_p9.ui.fragment.PropertyListFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_connexion.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : BaseActivity(), PropertyListFragment.OnClickEstateListener, MapFragment.OnClickMarkerListener {

    //Fields
    private lateinit var mQuery: Query
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var  mAuth : FirebaseAuth

    companion object LoginUser {
        var LOGIN_USER: String = ""
    }
    /*****************************************************************************************
     * override method
     ****************************************************************************************/

    override fun getActivityLayout(): Int {
        return R.layout.activity_main
    }

    override fun getToolbar(): Toolbar? {
        return toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth= FirebaseAuth.getInstance()

        navController = findNavController(R.id.navHostFragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_property, R.id.logout, R.id.nav_map, R.id.nav_profile, R.id.nav_simulation
            ), drawer_layout
        )


        setupActionBarWithNavController(navController, appBarConfiguration)
        drawer_navigation.setupWithNavController(navController)

        LOGIN_USER = intent.getStringExtra("login")!!
        updateNavigationHeader()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    /**
     * use to control the back press
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Update header with user information
     */
    private fun updateNavigationHeader() {

        val headerNav = drawer_navigation.getHeaderView(0)

        //XML id for update data
        val imageViewNav = headerNav.findViewById<ImageView>(R.id.imageView_navHeader)
        val textViewNavName = headerNav.findViewById<TextView>(R.id.text_name)
        val textViewNavMail = headerNav.findViewById<TextView>(R.id.text_mail)
        val textViewNavtel = headerNav.findViewById<TextView>(R.id.text_phone_nav)

        mQuery = UsersHelpers().getAllUsers().whereEqualTo("login", LOGIN_USER)
        mQuery.get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (document: QueryDocumentSnapshot in it.result!!) {
                    Glide.with(this)
                        .load(document.get("avatar").toString())
                        .circleCrop()
                        .into(imageViewNav)
                    textViewNavName.text = document.get("surname").toString()
                    textViewNavMail.text = document.get("login").toString()
                    textViewNavtel.text = document.get("telephone").toString()

                }
            }
        }

    }

    override fun onClickItemEstate(propertyId: Long) {
        PROPERTY_ID_DETAIL = propertyId
        if (frameLayout_container_detail != null) {
            val fragmentDetail = DetailPropertyFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout_container_detail, fragmentDetail)
                .commit()
        } else {
            navController.navigate(R.id.action_nav_property_to_detailPropertyFragment)
        }
    }

    override fun onClickMarkerEstate(propertyId: Long) {
        PROPERTY_ID_DETAIL = propertyId
        navController.navigate(R.id.detailPropertyFragment)
    }

    fun logOutApplication(item: MenuItem) {
        when (item.itemId) {
            R.id.logout -> {
                val pref = getSharedPreferences("SharedConnection", Context.MODE_PRIVATE).edit()
                pref.putBoolean("pref_check", false)
                    .putString("pref_pass", "")
                    .apply()
                finish()
                mAuth.signOut()
            }
        }
    }
}
