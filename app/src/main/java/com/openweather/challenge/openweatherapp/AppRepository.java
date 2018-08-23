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


    public void getCurrentWeather(String location){ //Livedata?
        mNetworkDataSource.getCurrentWeather(location);
    }

    public LiveData<List<WeatherEntity>> getAllWeather(){

        return mWeatherDao.getAllWeathers();
    }

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

    public List<CityEntity> getCitiesByName(String text){
        try {
            return new RetrieveCitiesByName(mCityDao).execute(text).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertDummyWeather(){
        //Remember use a separate thread when you insert elements into database
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mWeatherDao.insert(new WeatherEntity(442, 123.45, 123.45, 123, "frio", "descripcion", "icon",
                    "base", 25.45, 45, 13, 12.45, 45.12, 1, 45.12, 1, 1,
                    11254564, 5, 4, 45.4, "PY", 123456, 1321645, "name", 200));
            OpenWeatherApp.Logger.d("Dummy object inserted");
        });
    }

    public void deleteDummyWeather(){
        //Remember use a separate thread when you insert elements into database
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mWeatherDao.deleteDummy();
            OpenWeatherApp.Logger.d("Dummy object deleted");
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
