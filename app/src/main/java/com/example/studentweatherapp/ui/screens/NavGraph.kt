package com.example.studentweatherapp.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.studentweatherapp.data.TAG
import com.google.android.gms.maps.model.LatLng

// Организуем навигацию между экранами приложения
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    currentLocation: String
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        // Экран с отображением погоды и прогноза (в него передается строка с
        // координатами местоположения для корректного отображения погоды)
        composable(
            route = Screens.Home.route,
            arguments = listOf(navArgument(LOCATION_ARGUMENT_KEY) {
                type = NavType.StringType
            })
        ) {
            HomeScreen(
                navController = navController,
                locationArgument = it.arguments?.getString(LOCATION_ARGUMENT_KEY).toString()
            )
        }

        // Экран со списком избранных городов.
        composable(
            route = Screens.Favorites.route
        ) {
            FavoritesScreen(navController = navController)
        }

        // Экран со списком избранных городов.
        composable(
            route = Screens.Map.route,
            arguments = listOf(navArgument(LOCATION_ARGUMENT_KEY) {
                type = NavType.StringType
            })
        ) {
            MapScreen(
                navController = navController,
                locationArgument = it.arguments?.getString(LOCATION_ARGUMENT_KEY).toString()
                )
        }
    }

    // Переходим к экрану с погодой
    navController.navigate(Screens.Home.passLocation(currentLocation))
}

