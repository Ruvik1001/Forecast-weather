package com.example.studentweatherapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations_table")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val locationName: String,
    val locationLatLong: String
)

@Entity(tableName = "favorite_table")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val favoriteName: String,
    val favoriteLatLong: String
)