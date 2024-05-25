package com.example.studentweatherapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    locationViewModel: LocationViewModel = viewModel(factory = LocationViewModel.factory)
) {
    val itemsList = locationViewModel.itemsList.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Поле ввода названия населенного пункта.
            TextField(
                value = locationViewModel.newText.value,
                onValueChange = {
                    locationViewModel.newText.value = it
                },
                label = {
                    Text(text = "Название...")
                },
                modifier = Modifier.weight(1f)
            )

            // Кнопка добавления населенного пункта.
            IconButton(
                onClick = {
                    locationViewModel.insertItem()
                },
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "addLocationIcon"
                )
            }

        }

        // Разделитель между полем ввода и списком сохраненных н.п.
        Spacer(modifier = Modifier.height(5.dp))

        // Столбец со списком сохраненных населенных пунктов.
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(itemsList.value) { item ->
                LocationListItem(
                    item,
                    {
                        navController.navigate(Screens.Home.passLocation(it.locationName)) {
                            popUpTo("home_screen") {
                                inclusive = true
                            }
                        }
                    },
                    {
                        locationViewModel.locationEntity = it
                        locationViewModel.newText.value = it.locationName
                    },
                    {
                        locationViewModel.deleteItem(it)
                    }
                )
            }
        }
    }
}

