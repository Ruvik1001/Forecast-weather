package com.example.studentweatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.studentweatherapp.data.TAG
import com.example.studentweatherapp.ui.screens.SetupNavGraph
import com.example.studentweatherapp.ui.service.presentation.WeatherUpdateService
import com.example.studentweatherapp.ui.theme.StudentWeatherAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationCallback

lateinit var locationCallback: LocationCallback


@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {

    private lateinit var permissionRequestLauncher: ActivityResultLauncher<Array<String>>

    @SuppressLint("ShortAlarm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        checkPermissions()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val isServiceRunning = prefs.getBoolean("isServiceRunning", false)
        if (!isServiceRunning) {
            // Запуск службы при старте приложения
            val serviceIntent = Intent(this, WeatherUpdateService::class.java)
            startService(serviceIntent)

            // Установка флага о том, что служба запущена
            prefs.edit().putBoolean("isServiceRunning", true).apply()
        }

        // Запуск периодического обновления данных каждый час
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, WeatherUpdateService::class.java)
        val pendingIntent = PendingIntent.getService(
            this,
            0,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intervalMillis = AlarmManager.INTERVAL_HOUR // интервал в миллисекундах (1 час)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + intervalMillis,
            intervalMillis,
            pendingIntent
        )

        permissionRequestLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            setContent {
                StudentWeatherAppTheme {
                    SetupNavGraph(
                        navController = rememberNavController(),
                        currentLocation = "0.0,0.0"
                    )
                }
            }
        }

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionRequestLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        } else {
            setContent {
                // Запускаем первоначальный экран

                val navController = rememberNavController()


                StudentWeatherAppTheme {
                    SetupNavGraph(
                        navController = navController,
                        currentLocation = "0.0,0.0"
                    )
                }
            }
        }
    }

    fun checkPermissions() {

    }

    fun handleLocation(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        Log.d(TAG, "handleLocation: Lat $latitude, Lon $longitude")
        Toast.makeText(this, "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_SHORT)
            .show()
        val prefs = this.getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("latitude", latitude.toString()).apply()
        prefs.edit().putString("longitude", longitude.toString()).apply()
    }
}
