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

import java.util.Date;
import java.util.List;

/**
 * Created by manuel on 21,August,2018
 */
@Dao // Required annotation for Dao to be recognized by Room
public interface WeatherDao {

    @Query("SELECT * FROM weather_table")
    LiveData<List<WeatherEntity>> getAllWeathers();

    // Inserts single weather
    @Insert
    void insert(WeatherEntity weather);

    // Inserts multiple weather
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WeatherEntity> weathers);

    // Deletes all weather from the database
    @Query("DELETE FROM weather_table")
    void deleteAll();

    /**
     * Selects all {@link WeatherEntity} entries after a give date, inclusive. The LiveData will
     * be kept in sync with the database, so that it will automatically notify observers when the
     * values in the table change.
     *
     * @param date A {@link Date} from which to select all future weather
     * @return {@link LiveData} list of all {@link WeatherEntity} objects after date
     */
    @Query("SELECT * FROM weather_table WHERE dt >= :date")
    LiveData<List<WeatherEntity>> getCurrentWeather(long date);

    /**
     * Inserts a list of {@link WeatherEntity} into the weather table. If there is a conflicting id
     * or date the weather entry uses the {@link OnConflictStrategy} of replacing the weather
     * forecast. The required uniqueness of these values is defined in the {@link WeatherEntity}.
     *
     * @param weather A list of weather forecasts to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(WeatherEntity... weather);

    /**
     * Deletes any weather data older than the given day
     *
     * @param date The date to delete all prior weather from (exclusive)
     */
    @Query("DELETE FROM weather_table WHERE dt < :date")
    void deleteOldWeather(long date);

//
//    @Query("SELECT * FROM weather WHERE date = :date")
//    WeatherEntity getWeatherByDate(Date date);

    @Query("DELETE FROM weather_table WHERE id in (SELECT id FROM weather_table LIMIT 1)")
    void deleteDummy();

    @Delete
    void delete(WeatherEntity weatherEntity);

}
