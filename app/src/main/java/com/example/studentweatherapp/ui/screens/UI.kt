package com.example.studentweatherapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.studentweatherapp.R
import com.example.studentweatherapp.data.LocationEntity
import com.example.studentweatherapp.data.WeatherModel
import com.example.studentweatherapp.ui.theme.BlueLight

@Composable
fun MainList(list: List<WeatherModel>, currentDay: MutableState<WeatherModel>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(
            list
        ) { _, item ->
            WeatherListItem(item, currentDay)
        }
    }
}

@Composable
fun WeatherListItem(item: WeatherModel, currentDay: MutableState<WeatherModel>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp)
            .clickable {
                if (item.hours.isEmpty()) return@clickable
                currentDay.value = item
            },
        colors = CardDefaults.cardColors(containerColor = BlueLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 5.dp,
                    bottom = 5.dp
                )
            ) {
                Text(text = item.time)
                Text(
                    text = item.condition,
                    color = Color.White
                )
            }
            Text(
                text = item.currentTemp.ifEmpty { "${item.maxTemp}/${item.minTemp}" },
                color = Color.White,
                style = TextStyle(fontSize = 25.sp)
            )
            AsyncImage(
                model = "https:${item.icon}",
                contentDescription = "listWeatherIcon",
                modifier = Modifier
                    .size(35.dp)
                    .padding(
                        top = 3.dp,
                        end = 8.dp
                    )
            )
        }
    }
}

@Composable
fun LocationListItem(
    item: LocationEntity,
    onClick: (LocationEntity) -> Unit,
    onEdit: (LocationEntity) -> Unit,
    onDelete: (LocationEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                onClick(item)
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Название населенного пункта.
            Text(
                text = item.locationName,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp)
            )

            // Кнопка редактирования населенного пункта в списке.
            IconButton(
                onClick = {
                    onEdit(item)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "editLocationIcon"
                )
            }

            // Кнопка удаления населенного пункта из списка.
            IconButton(
                onClick = {
                    onDelete(item)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "deleteLocationIcon"
                )
            }
        }
    }
}

// Элемент списка избранных городов.
@Composable
fun FavoriteListItem(

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {

            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Название избранного города.
            Text(
                text = "Item",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp)
            )

            // Кнопка удаления города из списка.
            IconButton(
                onClick = { /*TODO*/ }
            ) {
               Icon(
                   imageVector = Icons.Default.Delete,
                   contentDescription = "deleteFavoriteIcon"
               )
            }
        }
    }
}

@Composable
fun getWeatherConditionBackground(currentDay: MutableState<WeatherModel>): Int {
    val conditionBackground: Int = when (currentDay.value.condition) {
        "Умеренный дождь", "Местами дождь" -> R.drawable.partly_rain_weather_background
        "Умеренный или сильный снег", "Умеренный снег" -> R.drawable.partly_snow_weather_background
        else -> R.drawable.partly_cloudy_weather_background
    }
    return conditionBackground
}