/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.openweather.challenge.openweatherapp.entity.WeatherEntity;

import java.util.List;

/**
 * Created by manuel on 21,August,2018
 */
@Dao // Required annotation for Dao to be recognized by Room
public interface WeatherDao {

    @Query("SELECT * FROM weather_table ORDER BY name ASC")
    LiveData<List<WeatherEntity>> getAllWeathers();

    // Inserts single weather
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeatherEntity weather);

    // Inserts multiple weather
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WeatherEntity> weathers);

    // Deletes all weather from the database
    @Query("DELETE FROM weather_table")
    void deleteAll();


    // Deletes a single city
    @Delete
    void delete(WeatherEntity weather);

//
//    @Query("SELECT * FROM weather WHERE date = :date")
//    WeatherEntity getWeatherByDate(Date date);

}
