package com.example.studentweatherapp.ui.service.shared

import com.example.studentweatherapp.ui.service.data.WidgetWeatherModel
import com.google.gson.Gson

fun serializeWeatherModel(weatherModel: WidgetWeatherModel): String {
    return Gson().toJson(weatherModel)
}

fun deserializeWeatherModel(jsonString: String): WidgetWeatherModel? {
    return try {
        Gson().fromJson(jsonString, WidgetWeatherModel::class.java)
    } catch (e: Exception) {
        null
    }
}
