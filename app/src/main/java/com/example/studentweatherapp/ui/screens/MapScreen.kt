package com.example.studentweatherapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.studentweatherapp.data.SimpleWebBrowser

@Composable
fun MapScreen(
    navController: NavController,
    locationArgument: String
) {
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            SimpleWebBrowser(url = "https://www.ventusky.com/?p=$locationArgument;10&l=rain-3h")
        }
        // Кнопка возврата на экран текущей погоды.
        Button(
            onClick = {
                navController.navigate(Screens.Home.passLocation(locationArgument)) {
                    popUpTo("home_screen") {
                        inclusive = true
                    }
                }
            },

        ) {
            Text(text = "Назад")
        }
    }
}