package com.example.studentweatherapp.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.studentweatherapp.App
import com.example.studentweatherapp.data.LocationEntity
import com.example.studentweatherapp.data.WeatherDb
import kotlinx.coroutines.launch

class LocationViewModel(val database: WeatherDb) : ViewModel() {

    val itemsList = database.dao.getAllLocations()
    val newText = mutableStateOf("")
    var locationEntity: LocationEntity? = null

    fun insertItem() = viewModelScope.launch {
        val locationItem = locationEntity?.copy(locationName = newText.value) ?: LocationEntity(
            locationName = newText.value,
            locationLatLong = ""
        )
        database.dao.insertLocation(locationItem)
        locationEntity = null
        newText.value = ""
    }

    fun deleteItem(item: LocationEntity) = viewModelScope.launch {
        database.dao.deleteLocation(item)
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as App).locationDatabase
                return LocationViewModel(database) as T
            }
        }
    }
}