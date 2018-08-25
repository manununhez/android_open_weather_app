/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.repository;

import android.arch.lifecycle.LiveData;

import com.openweather.challenge.openweatherapp.OpenWeatherApp;
import com.openweather.challenge.openweatherapp.db.dao.WeatherDao;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.network.NetworkDataSource;
import com.openweather.challenge.openweatherapp.utils.OpenWeatherDateUtils;
import com.openweather.challenge.openweatherapp.utils.Utils;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by manuel on 21,August,2018
 */
public class AppRepository {
    // For Singleton instantiation
    private static AppRepository INSTANCE;
    private final WeatherDao mWeatherDao;
    private final NetworkDataSource mNetworkDataSource;
    private boolean mInitialized = false;

    private AppRepository(WeatherDao weatherDao, NetworkDataSource networkDataSource) {
        mWeatherDao = weatherDao;
        mNetworkDataSource = networkDataSource;

        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        // Group of weathers that corresponds to the weather of all cities saved in the DB
        LiveData<WeatherEntity[]> newWeathersFromNetwork = responseFromGetCurrentWeathers();

        newWeathersFromNetwork.observeForever(weathersFromNetwork -> {
            Executors.newSingleThreadScheduledExecutor().execute(() -> {
                // Deletes old historical data
                //deleteOldData();
                OpenWeatherApp.Logger.d("Old weather deleted");
                // Insert our new weather data into OpenWeatherApp's database
                bulkInsert(weathersFromNetwork);

                OpenWeatherApp.Logger.d("New values inserted");
            });
        });
    }



    public synchronized static AppRepository getInstance(WeatherDao weatherDao, NetworkDataSource networkDataSource) {
        OpenWeatherApp.Logger.d("Getting the repository");
        if (INSTANCE == null) {
            synchronized (AppRepository.class) {
                INSTANCE = new AppRepository(weatherDao, networkDataSource);
                OpenWeatherApp.Logger.d("Made new repository");
            }
        }
        return INSTANCE;
    }

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     */
    private synchronized void initializeData() {

        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (mInitialized) return;
        mInitialized = true;

        // This method call triggers OpenWeatherApp to create its task to synchronize weather data
        // periodically.
        mNetworkDataSource.scheduleRecurringFetchWeatherSync();

        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            if (isFetchNeeded()) {
                deleteOldData();
                startFetchWeatherService();
            }
        });
    }

    /******************************
     * DB related operation
     *****************************/

    /**
     * Checks if at least exist one weather entry, that would be mean that exists at least one selected and stored weather-city in the DB,
     * and the lastUpdateTime should not be older than the time of SYNC_INTERVAL ({@link NetworkDataSource})
     *
     * @return Whether a fetch is needed
     */
    private boolean isFetchNeeded() {
        long now = OpenWeatherDateUtils.getUnixTimeNowInSecs();
        int count = getCountCurrentWeathers();
        long lastUpdate = getLastUpdateTime();
        return (count > 0 && isSyncNeeded(now, lastUpdate));
    }

    /**
     * @return
     */
    public long getLastUpdateTime() {
        return mWeatherDao.getLastUpdateTime();
    }

    /**
     * @return
     */

    public List<Integer> getAllWeathersId() {
        return mWeatherDao.getAllWeathersId();
    }


    /**
     * @return
     */
    public int getCountCurrentWeathers() {
        return mWeatherDao.getCountCurrentWeathers();
    }

    /**
     * @param weathersFromNetwork
     */
    private void bulkInsert(WeatherEntity[] weathersFromNetwork) {
        mWeatherDao.bulkInsert(weathersFromNetwork);
    }


    /**
     * @param weatherEntity
     */

    public void insertWeather(WeatherEntity weatherEntity) {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mWeatherDao.insert(weatherEntity);
            OpenWeatherApp.Logger.d("Weather object inserted: " + weatherEntity.toString());
        });
    }

    /**
     * Deletes old weather data because we don't need to keep multiple days' data
     */
    private void deleteOldData() {
        long today = OpenWeatherDateUtils.getNormalizedUtcSecondsForToday();
        //Remember use a separate thread when you insert/delete elements into database
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mWeatherDao.deleteOldWeather(today);
        });
    }

    /**
     * Delete an specific weather item
     *
     * @param item
     */
    public void delete(WeatherEntity item) {
        //Remember use a separate thread when you insert/delete elements into database
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mWeatherDao.delete(item);
        });
    }


    /**
     * @return
     */
    public LiveData<List<WeatherEntity>> getCurrentWeathers() {
        initializeData();
        return mWeatherDao.getCurrentWeather();
    }


    /******************************
     * Network related operation
     *****************************/

    /**
     *
     * @return
     */
    private LiveData<WeatherEntity[]> responseFromGetCurrentWeathers() {
        return mNetworkDataSource.responseFromGetCurrentWeathers();
    }

    /**
     *
     */
    private void startFetchWeatherService() {
        mNetworkDataSource.startFetchWeatherService();
    }


    private boolean isSyncNeeded(long now, long lastUpdate) {
        return mNetworkDataSource.isSyncNeeded(now, lastUpdate);
    }

    /**
     * fetchAndInsertWeather from network by city name
     *
     * @param cityName
     */
    public void getWeatherByCityName(String cityName) {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mNetworkDataSource.fetchCurrentWeatherByCityName(cityName);
        });
    }

    /**
     * @return
     */
    public LiveData<WeatherEntity> responseWeatherByCityName() {
        return mNetworkDataSource.responseWeatherByCityName();
    }


    /**
     *
     */
    public void fetchCurrentWeathersByCityIDs() {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            List<Integer> list = getAllWeathersId(); //We get the list of weather ID from the DB
            String idList = Utils.listToCommaValues(list);

            mNetworkDataSource.fetchCurrentWeathersByCitiesID(idList);
        });
    }


}
