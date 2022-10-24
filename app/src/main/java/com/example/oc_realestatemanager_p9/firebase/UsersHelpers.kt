package com.example.oc_realestatemanager_p9.firebase

import com.example.oc_realestatemanager_p9.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class UsersHelpers {

    //FIELD
    private val COLLECTION_NAME = "users"
    var db = FirebaseFirestore.getInstance()

    // --- COLLECTION REFERENCE ---
    fun getUsersCollection(): CollectionReference {
        return db.collection(COLLECTION_NAME)
    }

    // --- CREATE ---
      fun createUser(id:Int, name: String, surname: String, avatar : String, mail :String, login: String, password: String, telephone: String) : Task<Void> {
        val userToCreate = User(id,name, surname, avatar, mail, login, password, telephone)
        return UsersHelpers().getUsersCollection().document().set(userToCreate)
    }

    // -- GET ALL Users --
    fun getAllUsers(): Query {
        return UsersHelpers().getUsersCollection()
    }

    // --- UPDATE ---

    fun updateUser(password : String, mail : String, avatar : String, uid : String, telephone : String) {

        val data : MutableMap<String, Any> = HashMap()
        data["password"] = password
        data["telephone"] = telephone
        data["login"] = mail
        data["avatar"] = avatar
        UsersHelpers().getUsersCollection().document(uid).update(data)
    }

}

