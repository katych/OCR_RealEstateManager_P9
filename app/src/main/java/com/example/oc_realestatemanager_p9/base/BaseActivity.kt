package com.example.oc_realestatemanager_p9.base

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.ButterKnife
import com.google.firebase.auth.FirebaseAuth


abstract class BaseActivity : AppCompatActivity() {

    // fields
    private lateinit var mFirebaseAuth: FirebaseAuth

    // Methods
    abstract fun getActivityLayout(): Int

    @Nullable
    protected abstract fun getToolbar(): Toolbar?

    // --------------------
    // Activity
    // --------------------
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(getActivityLayout())
        ButterKnife.bind(this) //Configure Butterknife
        this.configureToolBar()
        this.configureFirebaseAuth() // configure firebase


    }

    /**
     * Configures the [FirebaseAuth]
     */
    protected open fun configureFirebaseAuth() {
        mFirebaseAuth = FirebaseAuth.getInstance()
    }

    // --------------------
    // UI
    // --------------------

    protected open fun configureToolBar(text: String? = "") { // If ToolBar exists
        if (getToolbar() != null) {
            getToolbar()!!.title = text
            setSupportActionBar(getToolbar())
        }
    }


}