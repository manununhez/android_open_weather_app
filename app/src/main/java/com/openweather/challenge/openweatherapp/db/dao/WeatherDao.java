package com.openweather.challenge.openweatherapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;

import java.util.List;

/**
 * Created by manuel on 21,August,2018
 */
@Dao // Required annotation for Dao to be recognized by Room
public interface WeatherDao {

    /**
     * List of the IDs of the weather entries (without pulling all of the data.)
     *
     * @return {@link Integer} list of ID of all weather entries
     */
    @Query("SELECT id FROM weather_table")
    List<Integer> getAllWeathersId();

    /**
     * Last update. We get the oldest dt, that's why we order dt by ASC. This is key to know if new
     * data should be fetched from network.
     *
     * @return {@link long} the oldest datetime entry in the database
     */
    @Query("SELECT dt FROM weather_table ORDER by dt ASC LIMIT 1")
    long getLastUpdateTime();

    /**
     * Selects all ids entries. This is for easily seeing
     * what entries are in the database without pulling all of the data.
     */
    @Query("SELECT count(*) FROM weather_table")
    int getCountCurrentWeathers();

    /**
     * Insert a single {@link WeatherEntity} into the database
     *
     * @param weather
     */
    @Insert
    void insert(WeatherEntity weather);


    /**
     * Selects all {@link WeatherEntity} entries order by name. The LiveData will
     * be kept in sync with the database, so that it will automatically notify observers when the
     * values in the table change.
     *
     * @return {@link LiveData} list of all {@link WeatherEntity} objects order by name
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


    /**
     * Deletes any weather data older than some date
     *
     * @param date The date to delete all prior weather from (exclusive)
     */
    @Query("DELETE FROM weather_table WHERE dt < :date")
    void deleteOldWeather(long date);

    /**
     * Delete a single {@link WeatherEntity} from the database
     */
    @Delete
    void delete(WeatherEntity weatherEntity);


}
