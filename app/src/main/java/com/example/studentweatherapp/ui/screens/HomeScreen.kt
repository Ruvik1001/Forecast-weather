package com.example.studentweatherapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.studentweatherapp.data.GetLocation
import com.example.studentweatherapp.data.TAG
import com.example.studentweatherapp.data.WeatherModel
import com.example.studentweatherapp.data.getWeather
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng

@Composable
fun HomeScreen(
    navController: NavController,
    locationArgument: String
) {
    val context = LocalContext.current


    // Отслеживание прогноза погоды
    val daysList = remember {
        mutableStateOf(listOf<WeatherModel>())
    }

    // Отслеживание текущей погоды
    val currentDay = remember {
        mutableStateOf(
            WeatherModel(
                "",
                region = "",
                country = "",
                lat = "",
                lon = "",
                "",
                "0.0",
                "",
                "",
                "0.0",
                "0.0",
                "0",
                "N",
                "0",
                "0",
                ""
            )
        )
    }

    // Отслеживание фонового изображения текущей погоды


    GetLocation(daysList, currentDay)

    // Получаем данные о текущей погоде и прогноз
    Log.d(TAG, "HomeScreen: locationArgument = $locationArgument")
    if (locationArgument != "0.0,0.0") {
        getWeather(
            location = locationArgument,
            context = LocalContext.current,
            daysList = daysList,
            currentDay = currentDay
        )
    }

    // Фоновое отображение текущей погоды
    Image(
        painter = painterResource(
            id = getWeatherConditionBackground(currentDay = currentDay)
        ),
        contentDescription = "appBackgroundImage",
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.75f),
        contentScale = ContentScale.FillBounds
    )

    // Отображаем текущую погоду и прогноз.
    Column {

        // Карточка с текущей погодой.
        MainCard(
            currentDay = currentDay,
            onClickSync = {
                getWeather(
                    location = locationArgument,
                    context = context,
                    daysList = daysList,
                    currentDay = currentDay
                )
            },
            onClickFavorite = {
                navController.navigate(Screens.Favorites.route)
            },
            onClickMap = {
                navController.navigate(Screens.Map.passLocation(
                    "${currentDay.value.lat.toString()};" +
                            "${currentDay.value.lon.toString()}"))
            }
        )

        // Прогноз по дням или часам
        TabLayout(
            dayList = daysList,
            currentDay = currentDay
        )
    }
}