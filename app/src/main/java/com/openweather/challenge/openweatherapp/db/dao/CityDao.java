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

import com.openweather.challenge.openweatherapp.entity.CityEntity;

import java.util.List;

/**
 * Created by manuel on 21,August,2018
 */
@Dao // Required annotation for Dao to be recognized by Room
public interface CityDao {
    @Query("SELECT * FROM city_table WHERE name LIKE :dealText")
    List<CityEntity> getCitiesByName(String dealText);

    // Returns a list of all cities in the database
    @Query("SELECT * FROM city_table ORDER BY name ASC")
    List<CityEntity> getAllCities();
//    LiveData<List<CityEntity>> getAllCities();

    // Inserts single city
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CityEntity city);

    // Inserts multiple cities
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CityEntity... cities);

    // Deletes all cities from the database
    @Query("DELETE FROM city_table")
    void deleteAll();

    // Deletes a single city
    @Delete
    void delete(CityEntity city);




}
