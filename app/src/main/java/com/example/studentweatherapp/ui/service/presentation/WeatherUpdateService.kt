package com.example.studentweatherapp.ui.service.presentation

import android.app.IntentService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.studentweatherapp.data.API_KEY
import com.example.studentweatherapp.data.TAG
import com.example.studentweatherapp.ui.service.presentation.WeatherWidget.Companion.updateAppWidget
import com.example.studentweatherapp.ui.service.api.WeatherApiService
import com.example.studentweatherapp.ui.service.data.WidgetWeatherModel
import com.example.studentweatherapp.ui.service.shared.loadWeatherModel
import com.example.studentweatherapp.ui.service.shared.saveWeatherModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val SERVICE_WEATHER_WIDGET_UPDATE_NAME = "WeatherUpdateService"

class WeatherUpdateService : IntentService(SERVICE_WEATHER_WIDGET_UPDATE_NAME) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherApiService = retrofit.create(WeatherApiService::class.java)

    override fun onHandleIntent(intent: Intent?) {
        fetchWeatherData(applicationContext) {
            if (it == null) return@fetchWeatherData
            updateWidgetWithWeatherData(it)
        }
    }

    private fun fetchWeatherData(applicationContext: Context, callback: (WidgetWeatherModel?)->Unit) {
        val cur = loadWeatherModel(applicationContext)?.location?.name?: DEFAULT_CITY
        val call = weatherApiService.getWeatherForecast(API_KEY, cur)

        call.enqueue(object : Callback<WidgetWeatherModel> {
            override fun onResponse(call: Call<WidgetWeatherModel>, response: Response<WidgetWeatherModel>) {
                if (response.isSuccessful) {
                    val weatherApiResponse = response.body()
                    if (weatherApiResponse != null) {

                        saveWeatherModel(applicationContext, weatherApiResponse)
                        callback(weatherApiResponse)
                    }
                }
            }

            override fun onFailure(call: Call<WidgetWeatherModel>, t: Throwable) {
                Log.e(TAG, "fetchWeatherData failure: ${t.message.toString()}")
                callback(null)
            }
        })
    }

    private fun updateWidgetWithWeatherData(weatherData: WidgetWeatherModel) {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(applicationContext, WeatherWidget::class.java)
        )

        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(this, appWidgetManager, appWidgetId, weatherData)
        }
    }

    companion object {
        private const val API_BASE_URL = "https://api.weatherapi.com/v1/"
        private const val DEFAULT_CITY = "Moscow"
    }
}