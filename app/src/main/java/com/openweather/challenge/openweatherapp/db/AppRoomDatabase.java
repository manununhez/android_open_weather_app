package com.openweather.challenge.openweatherapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.openweather.challenge.openweatherapp.OpenWeatherApp;
import com.openweather.challenge.openweatherapp.db.dao.WeatherDao;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;

/**
 * Created by manuel on 21,August,2018
 */

/**
 * {@link AppRoomDatabase} database for the application including a table for {@link WeatherEntity}
 * with the DAO {@link WeatherDao}.
 */

// List of the entry classes
@Database(entities = {WeatherEntity.class}, version = 1)
public abstract class AppRoomDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "weather_database";
    // For Singleton instantiation
    private static AppRoomDatabase INSTANCE;

    public static AppRoomDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, AppRoomDatabase.DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();

                    OpenWeatherApp.Logger.d("Created a New Database ");


                }
            }
        }
        return INSTANCE;
    }

    // The associated DAOs for the database
    public abstract WeatherDao weatherDao();

}
