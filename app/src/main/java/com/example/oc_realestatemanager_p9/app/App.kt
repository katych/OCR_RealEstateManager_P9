package com.example.oc_realestatemanager_p9.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.oc_realestatemanager_p9.database.Database
import com.example.oc_realestatemanager_p9.google_map.AddressCoordonateApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application() {

    companion object{
        lateinit var db: Database
        lateinit var channelId : String
        lateinit var appContext : Context

        private val okhttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

        private val retrofit = Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl("https://maps.googleapis.com/maps/api/geocode/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

       val addressCoordonateApiService: AddressCoordonateApiService = retrofit.create(AddressCoordonateApiService::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())

        appContext = applicationContext

        db = Database.getDatabase(appContext)
        channelId = "com.example.oc_realestatemanager_p9.app"

        createNotificationChannel()

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel notification add"
            val descriptionText = "Notification add property"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}