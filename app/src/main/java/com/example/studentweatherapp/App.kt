package com.example.studentweatherapp

import android.app.Application
import com.example.studentweatherapp.data.WeatherDb

class App : Application() {
    val locationDatabase by lazy { WeatherDb.createDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        context = this


    }

    companion object {
        lateinit var context: App
            private set
    }
}