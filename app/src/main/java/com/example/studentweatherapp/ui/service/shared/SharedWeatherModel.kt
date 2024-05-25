package com.example.studentweatherapp.ui.service.shared

import android.content.Context
import com.example.studentweatherapp.ui.service.data.WidgetWeatherModel

fun saveWeatherModel(context: Context, weatherModel: WidgetWeatherModel) {
    val sharedPreferences = context.getSharedPreferences(SHARED_WIDGET_WEATHER_PATH, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val serializedModel = serializeWeatherModel(weatherModel)
    editor.putString(SHARED_WIDGET_WEATHER_KEY, serializedModel)
    editor.apply()
}

fun loadWeatherModel(context: Context): WidgetWeatherModel? {
    val sharedPreferences = context.getSharedPreferences(SHARED_WIDGET_WEATHER_PATH, Context.MODE_PRIVATE)
    val serializedModel = sharedPreferences.getString(SHARED_WIDGET_WEATHER_KEY, "")
    return if (serializedModel.isNullOrEmpty()) {
        null
    } else {
        deserializeWeatherModel(serializedModel)
    }
}

private const val SHARED_WIDGET_WEATHER_PATH = "com.example.studentweatherapp.PREFERENCES"
private const val SHARED_WIDGET_WEATHER_KEY = "WidgetWeatherModel"
