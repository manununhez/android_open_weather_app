/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.openweather.challenge.openweatherapp.db.dao.CityDao;
import com.openweather.challenge.openweatherapp.db.dao.WeatherDao;
import com.openweather.challenge.openweatherapp.entity.CityEntity;
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.network.NetworkDataSource;
import com.openweather.challenge.openweatherapp.utils.OpenWeatherDateUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Created by manuel on 21,August,2018
 */
public class AppRepository {
    // For Singleton instantiation
    private static AppRepository INSTANCE;
    private final CityDao mCityDao;
    private final WeatherDao mWeatherDao;
    private final NetworkDataSource mNetworkDataSource;

    public AppRepository(CityDao cityDao, WeatherDao weatherDao, NetworkDataSource networkDataSource) {
        mCityDao = cityDao;
        mWeatherDao = weatherDao;
        mNetworkDataSource = networkDataSource;

        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        // Group of weathers that corresponds to the weather of all cities saved in the DB
        LiveData<WeatherEntity[]> newWeathersFromNetwork = mNetworkDataSource.getCurrentWeathers();

        newWeathersFromNetwork.observeForever(weathersFromNetwork -> {
            Executors.newSingleThreadScheduledExecutor().execute(() -> {
                // Deletes old historical data
                deleteOldData();
                OpenWeatherApp.Logger.d("Old weather deleted");
                // Insert our new weather data into Sunshine's database
                mWeatherDao.bulkInsert(weathersFromNetwork);
                OpenWeatherApp.Logger.d("New values inserted");
            });
        });


        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        // New weather fetch from network (by cityID or cityName)

        LiveData<WeatherEntity> newWeatherFromNetwork = mNetworkDataSource.getCurrentWeather();

        newWeatherFromNetwork.observeForever(weathersFromNetwork -> {
            Executors.newSingleThreadScheduledExecutor().execute(() -> {
                // Insert our new weather data into Sunshine's database
                mWeatherDao.insert(weathersFromNetwork);
                OpenWeatherApp.Logger.d("New value inserted");
            });
        });
    }

    public synchronized static AppRepository getInstance(
            CityDao cityDao, WeatherDao weatherDao, NetworkDataSource networkDataSource) {
        OpenWeatherApp.Logger.d("Getting the repository");
        if (INSTANCE == null) {
            synchronized (AppRepository.class) {
                INSTANCE = new AppRepository(cityDao, weatherDao, networkDataSource);
                OpenWeatherApp.Logger.d("Made new repository");
            }
        }
        return INSTANCE;
    }


    public LiveData<List<WeatherEntity>> getCurrentWeathers() {
        long today = OpenWeatherDateUtils.getNormalizedUtcSecondsForToday();
        return mWeatherDao.getCurrentWeather(today);
    }
//
//    public LiveData<List<WeatherEntity>> getAllWeather(){
//
//        return mWeatherDao.getAllWeathers();
//    }

    public List<CityEntity> getAllCities() {
        try {
            return new RetrieveAllCities(mCityDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CityEntity> getCitiesByName(String text) {
        try {
            return new RetrieveCitiesByName(mCityDao).execute(text).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void insertWeather(WeatherEntity weatherEntity) {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mWeatherDao.insert(weatherEntity);
            OpenWeatherApp.Logger.d("Weather object inserted: " + weatherEntity.toString());
        });
    }

    /**
     * fetchAndInsertWeather from network by cityID
     * @param cityID
     */
    public void insertWeather(String cityID) {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mNetworkDataSource.fetchCurrentWeatherByCityID(cityID);
        });
    }

    public void insertDummyWeather() {
        //Remember use a separate thread when you insert elements into database
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mWeatherDao.insert(new WeatherEntity(442, 123.45, 123.45, 123, "frio", "descripcion", "icon",
                    "base", 25.45, 45, 13, 12.45, 45.12, 1, 45.12, 1, 1,
                    1535013592, 5, 4, 45.4, "PY", 123456, 1321645, "name", 200));
            OpenWeatherApp.Logger.d("Dummy object inserted");
        });
    }

    public void deleteDummyWeather() {
        //Remember use a separate thread when you insert/delete elements into database
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mWeatherDao.deleteDummy();
            OpenWeatherApp.Logger.d("Dummy object deleted");
        });
    }


    /**
     * Deletes old weather data because we don't need to keep multiple days' data
     */
    private void deleteOldData() {
        long today = OpenWeatherDateUtils.getNormalizedUtcMsForToday();
        //Remember use a separate thread when you insert/delete elements into database
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
        mWeatherDao.deleteOldWeather(today);
        });
    }

    /**
     * Delete an specific weather item
     * @param item
     */
    public void delete(WeatherEntity item) {
        //Remember use a separate thread when you insert/delete elements into database
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mWeatherDao.delete(item);
        });
    }

    private static class RetrieveAllCities extends AsyncTask<Void, Void, List<CityEntity>> {
        CityDao cityDao;

        // only retain a weak reference to the activity
        RetrieveAllCities(CityDao cityDao) {
            this.cityDao = cityDao;
        }

        @Override
        protected List<CityEntity> doInBackground(Void... voids) {
            return cityDao.getAllCities();
        }

        @Override
        protected void onPostExecute(List<CityEntity> cityEntities) {
            super.onPostExecute(cityEntities);
        }
    }

    private static class RetrieveCitiesByName extends AsyncTask<String, Void, List<CityEntity>> {
        CityDao cityDao;

        // only retain a weak reference to the activity
        RetrieveCitiesByName(CityDao cityDao) {
            this.cityDao = cityDao;
        }

        @Override
        protected List<CityEntity> doInBackground(String... strings) {
            return cityDao.getCitiesByName(strings[0]);
        }

        @Override
        protected void onPostExecute(List<CityEntity> cityEntities) {
            super.onPostExecute(cityEntities);
        }
    }
}
