package com.example.studentweatherapp.ui.service.api

import com.example.studentweatherapp.ui.service.data.WidgetWeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("current.json")
    fun getWeatherForecast(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("lang") lang: String = "ru"
    ): Call<WidgetWeatherModel>
}
