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

    @Query("SELECT id FROM weather_table")
    List<Integer> getAllWeathersId();

    //Last update. We get the oldest dt, that's why we order dt by ASC
    @Query("SELECT dt FROM weather_table ORDER by dt ASC LIMIT 1")
    long getLastUpdateTime();

    /**
     * Selects all ids entries after a give date, inclusive. This is for easily seeing
     * what entries are in the database without pulling all of the data.
     */
    @Query("SELECT count(*) FROM weather_table")
    int getCountCurrentWeathers();

    // Inserts single weather
    @Insert
    void insert(WeatherEntity weather);

    // Inserts multiple weather
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WeatherEntity> weathers);


    /**
     * Selects all {@link WeatherEntity} entries after a give date, inclusive. The LiveData will
     * be kept in sync with the database, so that it will automatically notify observers when the
     * values in the table change.
     * // * @param date A {@link Date} from which to select all future weather WHERE dt >= :date
     *
     * @return {@link LiveData} list of all {@link WeatherEntity} objects after date
     */
    @Query("SELECT * FROM weather_table ORDER by name ASC ")
    LiveData<List<WeatherEntity>> getCurrentWeather();

    /**
     * Inserts a list of {@link WeatherEntity} into the weather table. If there is a conflicting id
     * or date the weather entry uses the {@link OnConflictStrategy} of replacing the weather
     * forecast. The required uniqueness of these values is defined in the {@link WeatherEntity}.
     *
     * @param weather A list of weather forecasts to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(WeatherEntity... weather);


    // Deletes all weather from the database
    @Query("DELETE FROM weather_table")
    void deleteAll();

    /**
     * Deletes any weather data older than the given day
     *
     * @param date The date to delete all prior weather from (exclusive)
     */
    @Query("DELETE FROM weather_table WHERE dt < :date")
    void deleteOldWeather(long date);


    @Delete
    void delete(WeatherEntity weatherEntity);


}
