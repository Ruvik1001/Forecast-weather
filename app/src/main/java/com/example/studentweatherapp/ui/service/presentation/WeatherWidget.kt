package com.example.studentweatherapp.ui.service.presentation

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import com.example.studentweatherapp.R
import com.example.studentweatherapp.data.TAG
import com.example.studentweatherapp.ui.service.data.WidgetWeatherModel
import com.squareup.picasso.Picasso

class WeatherWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        for (appWidgetId in appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
//            deleteTitlePref(context, appWidgetId)
        }
    }

    companion object {
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            weatherData: WidgetWeatherModel,
        ) {
            val views = RemoteViews(context.packageName, R.layout.weather_widget)
            views.setTextViewText(R.id.city_name_text, weatherData.location?.name?: NAME_ERROR)
            views.setTextViewText(R.id.temperature_text, (weatherData.current?.temp_c?: TEMP_ERROR).toString())

            try {
                Picasso.get()
                    .load("https:${weatherData.current?.condition?.icon ?: DEFAULT_IMG_URL}")
                    .into(views, R.id.weather_icon, intArrayOf(appWidgetId))

            } catch (e: Exception) {
                Log.e(TAG, "WeatherWidget:updateAppWidget ${e.message.toString()}")
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private const val NAME_ERROR = "Loading"
        private const val TEMP_ERROR = "..."
        private const val DEFAULT_IMG_URL = "//cdn.weatherapi.com/weather/64x64/night/113.png"
    }
}