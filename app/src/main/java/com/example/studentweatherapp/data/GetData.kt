package com.example.studentweatherapp.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.studentweatherapp.App
import com.example.studentweatherapp.ui.service.data.Condition
import com.example.studentweatherapp.ui.service.data.Current
import com.example.studentweatherapp.ui.service.data.WidgetWeatherModel
import com.example.studentweatherapp.ui.service.presentation.WeatherUpdateService
import com.example.studentweatherapp.ui.service.shared.saveWeatherModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import org.json.JSONArray
import org.json.JSONObject

const val API_KEY = "a12b67e9fe9e4258a21173344230512"
const val TAG = "MyLog"

// Получение текущей геопозиции
@OptIn(ExperimentalPermissionsApi::class)
fun getCurrentLocation1() {
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(App.context)
    if (ActivityCompat.checkSelfPermission(
            App.context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            App.context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
    }
    fusedLocationProviderClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            if (location != null) {
//                handleLocation(location)
            } else {
                Toast.makeText(App.context, "Failed to get location", Toast.LENGTH_SHORT).show()
            }
        }
}



@OptIn(ExperimentalPermissionsApi::class)
@SuppressWarnings("MissingPermission")
@Composable
fun GetLocation(
    daysList: MutableState<List<WeatherModel>>,
    currentDay: MutableState<WeatherModel>
) {
    val currentLocation = LocationModel()
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)
    val cancellationToken = CancellationTokenSource()

    var hasFineLocationPermission by remember { mutableStateOf(false) }
    var hasCoarseLocationPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasFineLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        hasCoarseLocationPermission = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (hasFineLocationPermission && hasCoarseLocationPermission) {
            fusedLocationClient
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken.token)
                .addOnCompleteListener {
                    Log.d(TAG, "GetLocation: Lat ${it.result.latitude}, Lon ${it.result.longitude}")
                    getWeather(
                        "${it.result.latitude},${it.result.longitude}",
                        context,
                        daysList,
                        currentDay
                    )
                    currentLocation.latitude = it.result.latitude
                    currentLocation.longitude = it.result.longitude
                }
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            hasFineLocationPermission = true
            hasCoarseLocationPermission = true

            fusedLocationClient
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken.token)
                .addOnCompleteListener {
                    Log.d(TAG, "GetLocation: Lat ${it.result.latitude}, Lon ${it.result.longitude}")
                    getWeather(
                        "${it.result.latitude},${it.result.longitude}",
                        context,
                        daysList,
                        currentDay
                    )
                    currentLocation.latitude = it.result.latitude
                    currentLocation.longitude = it.result.longitude
                }
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}

// Запрос текущей погоды и прогноза на 3 дня (в бесплатной версии API)
fun getWeather(
    location: String,
    context: Context,
    daysList: MutableState<List<WeatherModel>>,
    currentDay: MutableState<WeatherModel>
) {
    // Текст запроса
    val url =
        "https://api.weatherapi.com/v1/forecast.json?key=$API_KEY&q=$location&days=3" +
                "&aqi=no&alerts=no&lang=ru"
    // Запрос с использованием библиотеки Volley
    val queue = Volley.newRequestQueue(context)
    val mRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            val list = getWeatherByDays(response)
            currentDay.value = list[0] // данные о текущей погоде
            daysList.value = list // прогноз погоды по дням
            Log.d(
                TAG,
                "getWeather: response = ${
                    response.toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)
                }"
            )
            val currentWeatherModel = list[0]
            try {

                saveWeatherModel(
                    context.applicationContext,
                    WidgetWeatherModel(
                        location = com.example.studentweatherapp.ui.service.data.Location(
                            name = currentWeatherModel.city
                        ),
                        current = Current(
                            temp_c = currentWeatherModel.currentTemp.toDoubleOrNull() ?: 0.0,
                            condition = Condition(icon = "//cdn.weatherapi.com/weather/64x64/night/113.png")
                        )
                    )
                )
                val serviceIntent = Intent(context.applicationContext, WeatherUpdateService::class.java)
                context.applicationContext.startService(serviceIntent)
            } catch (e: Exception) { Log.e(TAG, "getWeather:saveWeatherModel ${e.message.toString()}") }
        },
        {
            Toast.makeText(context, "Город $location не найден", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "getWeather: error = $it")
        }
    )
    queue.add(mRequest)
}

// Извлечение данных о погоде, полученных в формате JSON
fun getWeatherByDays(response: String): List<WeatherModel> {
    if (response.isEmpty()) return listOf() // при неудачном запросе возвращается пустой список
    val list = ArrayList<WeatherModel>()
    // Перевод ответа в кодировку UTF-8 для корректного отображения кириллицы
    val mainObject = JSONObject(response.toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8))
    // Извлечение названия места (города)
    val city = mainObject.getJSONObject("location").getString("name")
    // Извлечение секции прогноза по дням
    val days = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
    // Формирование списка данных о погоде по дням
    for (i in 0 until days.length()) {
        val item = days[i] as JSONObject
        list.add(
            WeatherModel(
                city,
                region = "",
                country = "",
                lat = "",
                lon = "",
                item.getString("date"),
                "",
                item.getJSONObject("day").getJSONObject("condition")
                    .getString("text"),
                item.getJSONObject("day").getJSONObject("condition")
                    .getString("icon"),
                item.getJSONObject("day").getString("maxtemp_c"),
                item.getJSONObject("day").getString("mintemp_c"),
                item.getJSONObject("day").getString("maxwind_kph"),
                "",
                "",
                "",
                item.getJSONArray("hour").toString()
            )
        )
    }
    // Данные о текущей погоде первыми в списке дней
    list[0] = list[0].copy(
        region = mainObject.getJSONObject("location").getString("region"),
        country = mainObject.getJSONObject("location").getString("country"),
        lat = mainObject.getJSONObject("location").getString("lat"),
        lon = mainObject.getJSONObject("location").getString("lon"),
        time = mainObject.getJSONObject("current").getString("last_updated"),
        currentTemp = mainObject.getJSONObject("current").getString("temp_c"),
        windDir = mainObject.getJSONObject("current").getString("wind_dir"),
        windDegree = mainObject.getJSONObject("current").getString("wind_degree"),
        pressHg = mainObject.getJSONObject("current").getString("pressure_mb")
    )
    return list
}

// Извлечение данных о погоде по часам
fun getWeatherByHours(hours: String): List<WeatherModel> {
    if (hours.isEmpty()) return listOf()
    val hoursArray = JSONArray(hours)
    val list = ArrayList<WeatherModel>()
    for (i in 0 until hoursArray.length()) {
        val item = hoursArray[i] as JSONObject
        list.add(
            WeatherModel(
                "",
                region = "",
                country = "",
                lat = "",
                lon = "",
                item.getString("time"),
                item.getString("temp_c").toFloat().toInt().toString() + "°C",
                item.getJSONObject("condition").getString("text"),
                item.getJSONObject("condition").getString("icon"),
                "",
                "",
                item.getString("wind_kph").toFloat().toInt().toString() + " км/ч",
                item.getString("wind_dir"),
                item.getString("wind_degree"),
                "",
                ""
            )
        )
    }
    return list
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SimpleWebBrowser(url: String) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                loadUrl(url)
                settings.javaScriptEnabled = true // Включить JavaScript для интерактивности
                settings.setSupportZoom(true)
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        }
    )
}

interface WeatherRepository {
    fun getLat(): String
    fun setLat(string: String)
}