package com.example.studentweatherapp.ui.screens

const val LOCATION_ARGUMENT_KEY = "location"

sealed class Screens(val route: String) {
    object Home : Screens(route = "home_screen/{$LOCATION_ARGUMENT_KEY}") {
        fun passLocation(location: String): String {
            return this.route.replace(oldValue = "{$LOCATION_ARGUMENT_KEY}", newValue = location)
        }
    }
    object Favorites : Screens(route = "favorites_screen")
    object Map : Screens(route = "map_screen/{$LOCATION_ARGUMENT_KEY}") {
        fun passLocation(location: String): String {
            return this.route.replace(oldValue = "{$LOCATION_ARGUMENT_KEY}", newValue = location)
        }

    }
}