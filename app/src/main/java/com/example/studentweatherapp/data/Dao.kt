package com.example.studentweatherapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM locations_table ORDER BY locationName ASC")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(nameEntity: LocationEntity)

    @Delete
    suspend fun deleteLocation(nameEntity: LocationEntity)

    @Query("SELECT * FROM favorite_table ORDER BY favoriteName ASC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(nameEntity: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(nameEntity: FavoriteEntity)
}