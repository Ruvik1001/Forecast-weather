package com.example.studentweatherapp.data

data class WeatherModel(
    val city: String,
    val region: String,
    val country: String,
    val lat: String,
    val lon: String,
    val time: String,
    val currentTemp: String,
    val condition: String,
    val icon: String,
    val maxTemp: String,
    val minTemp: String,
    val windKph: String,
    val windDir: String,
    val windDegree: String,
    val pressHg: String,
    val hours: String
)
