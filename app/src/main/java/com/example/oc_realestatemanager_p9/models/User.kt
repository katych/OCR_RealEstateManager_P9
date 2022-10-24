package com.example.oc_realestatemanager_p9.models

data class User(val id :Int,
                val name : String,
                val surname : String,
                val login : String,
                var avatar : String?,
                var mail :String,
                var password : String,
                var telephone : String?)

