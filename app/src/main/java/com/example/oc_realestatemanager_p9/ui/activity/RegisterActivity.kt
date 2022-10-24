package com.example.oc_realestatemanager_p9.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import butterknife.OnClick
import com.example.oc_realestatemanager_p9.R
import com.example.oc_realestatemanager_p9.base.BaseActivity
import com.example.oc_realestatemanager_p9.utils.Utils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_connexion.*
import kotlinx.android.synthetic.main.activity_register.*
import timber.log.Timber


class RegisterActivity : BaseActivity() {

    private var name: String? = ""
    private var login: String? = ""
    private var pass: String? = ""
    private var avatar: String? = ""
    private var confirmPass: String? = ""
    private var phone: String? = ""
    private var mAuth = FirebaseAuth.getInstance()
     private var db = FirebaseFirestore.getInstance()

    override fun getActivityLayout(): Int {
        return R.layout.activity_register
    }

    override fun getToolbar(): Toolbar? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        floating_add_avatar_register.setOnClickListener {
            ImagePicker.with(this)
                .crop() //Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
           avatar_register.setText(uri.toString())
    }

    @OnClick(R.id.register_button_register)
    fun onClickRegisterButtonRegister(view: View) {
        if (checking()) {
            name = name_register.text.toString()
            login = mail_register.text.toString()
            pass = password_register.text.toString()
            confirmPass = confirm_register.text.toString()
            phone = tel_register.text.toString()
            avatar = avatar_register.text.toString()
            when (view.id) {
                R.id.register_button_register -> {
                    val user = hashMapOf(
                        "surname" to name,
                        "login" to login,
                        "telephone" to phone,
                        "avatar" to avatar,
                        "password" to pass
                    )
                    val Users = db.collection("users")
                    val query = Users.whereEqualTo("login", login).get().addOnSuccessListener {
                        if (it.isEmpty) {
                            mAuth.createUserWithEmailAndPassword(login!!, pass!!)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        Users.document(login!!).set(user)
                                        connected()

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(
                                            baseContext, "Authentication failed  .",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            Toast.makeText(
                                baseContext, "User already register.",
                                Toast.LENGTH_SHORT
                            ).show()
                            startMainActivity()
                        }
                    }
                }
            }
        }
    }



    private fun checking(): Boolean {
        if (name_register.text.toString().trim { it <= ' ' }
                .isNotEmpty() && mail_register.text.toString().trim { it <= ' ' }
                .isNotEmpty() && password_register.text.toString().trim { it <= ' ' }
                .isNotEmpty() && confirm_register.text.toString().trim { it <= ' ' }
                .isNotEmpty() && tel_register.text.toString().trim { it <= ' ' }
                .isNotEmpty() && avatar_register.text.toString().trim { it <= ' ' }
                .isNotEmpty()) {
            return true
        }
        return false
    }

    @SuppressLint("CommitPrefEdits")
    private fun connected() {
        val shares = getSharedPreferences("SharedConnection", Context.MODE_PRIVATE).edit()
        shares.putString("pref_login", login)
            .putString("pref_pass", pass)
            .apply()
        startMainActivity()
    }

    private fun startMainActivity() {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}




