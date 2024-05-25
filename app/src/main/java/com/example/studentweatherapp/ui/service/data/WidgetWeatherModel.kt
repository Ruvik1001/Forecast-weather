package com.example.studentweatherapp.ui.service.data

data class WidgetWeatherModel(
    val current: Current? = null,
    val location: Location? = null,
)

data class Current(
    val cloud: Int? = null,
    val condition: Condition? = null,
    val feelslike_c: Double? = null,
    val feelslike_f: Double? = null,
    val gust_kph: Double? = null,
    val gust_mph: Double? = null,
    val humidity: Int? = null,
    val is_day: Int? = null,
    val last_updated: String? = null,
    val last_updated_epoch: Int? = null,
    val precip_in: Double? = null,
    val precip_mm: Double? = null,
    val pressure_in: Double? = null,
    val pressure_mb: Double? = null,
    val temp_c: Double? = null,
    val temp_f: Double? = null,
    val uv: Double? = null,
    val vis_km: Double? = null,
    val vis_miles: Double? = null,
    val wind_degree: Int? = null,
    val wind_dir: String? = null,
    val wind_kph: Double? = null,
    val wind_mph: Double? = null,
)

data class Location(
    val country: String? = null,
    val lat: Double? = null,
    val localtime: String? = null,
    val localtime_epoch: Int? = null,
    val lon: Double? = null,
    val name: String? = null,
    val region: String? = null,
    val tz_id: String? = null,
)

data class Condition(
    val code: Int? = null,
    val icon: String? = null,
    val text: String? = null,
)