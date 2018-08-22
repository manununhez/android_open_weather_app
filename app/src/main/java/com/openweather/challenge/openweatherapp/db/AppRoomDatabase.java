/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.openweather.challenge.openweatherapp.OpenWeatherApp;
import com.openweather.challenge.openweatherapp.db.dao.CityDao;
import com.openweather.challenge.openweatherapp.db.dao.WeatherDao;
import com.openweather.challenge.openweatherapp.entity.CityEntity;
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.utils.AppDataJsonParser;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * Created by manuel on 21,August,2018
 */

/**
 * {@link AppRoomDatabase} database for the application including a table for {@link WeatherEntity}
 * with the DAO {@link WeatherDao} and a table for {@link CityEntity} with the DAO {@link CityDao}.
 */

// List of the entry classes
@Database(entities = {WeatherEntity.class, CityEntity.class}, version = 1)
public abstract class AppRoomDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "weather_database";

    // The associated DAOs for the database
    public abstract WeatherDao weatherDao();
    public abstract CityDao cityDao();

    // For Singleton instantiation
    private static AppRoomDatabase INSTANCE;


    public static AppRoomDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, AppRoomDatabase.DATABASE_NAME)
                            .addCallback(new Callback() {

                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                }

                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // Add a delay to simulate a long-running operation
                                    addDelay();

                                    //Remember use a separate thread when you insert elements into database
                                    Executors.newSingleThreadScheduledExecutor().execute(() -> {
                                        // Generate the data for pre-population
                                        AppRoomDatabase database = AppRoomDatabase.getInstance(context);
                                        CityEntity[] cities = new CityEntity[0];
                                        try {
                                            cities = AppDataJsonParser.loadCitiesFromAsset(context);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        //intert cities
                                        insertData(database, cities);
                                    });

                                }
                            })
                            .build();

                    OpenWeatherApp.Logger.d("Created a New Database ");


                }
            }
        }
        return INSTANCE;
    }

    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }


    private static void insertData(final AppRoomDatabase database, final CityEntity[] cities) {
        database.runInTransaction(() -> { //DB transaction
            database.cityDao().insertAll(cities);
            OpenWeatherApp.Logger.d("Cities added to the DB from json");
        });
    }



}
