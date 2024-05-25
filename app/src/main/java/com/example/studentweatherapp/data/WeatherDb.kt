package com.example.studentweatherapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        LocationEntity::class,
        FavoriteEntity::class
    ],
    version = 1
)

abstract class WeatherDb : RoomDatabase() {

    abstract val dao: Dao

    companion object{
        fun createDatabase(context: Context): WeatherDb {
            return Room.databaseBuilder(
                context = context,
                klass =  WeatherDb::class.java,
                name = "weather.db"
            ).build()
        }
    }
}