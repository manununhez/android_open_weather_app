/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp;

import android.arch.lifecycle.LiveData;

import com.openweather.challenge.openweatherapp.db.dao.CityDao;
import com.openweather.challenge.openweatherapp.db.dao.WeatherDao;
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;
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
    private final CityDao mCityDao;
    private final WeatherDao mWeatherDao;
    private final NetworkDataSource mNetworkDataSource;
    //    private final MutableLiveData<WeatherEntity> responseWeatherByCityName;
    private boolean mInitialized = false;

    public AppRepository(CityDao cityDao, WeatherDao weatherDao, NetworkDataSource networkDataSource) {
        mCityDao = cityDao;
        mWeatherDao = weatherDao;
        mNetworkDataSource = networkDataSource;
//        responseWeatherByCityName = new MutableLiveData<>();
//        // As long as the repository exists, observe the network LiveData.
//        // If that LiveData changes, update the database.
//        // Group of weathers that corresponds to the weather of all cities saved in the DB
        LiveData<WeatherEntity[]> newWeathersFromNetwork = mNetworkDataSource.getCurrentWeathers();

        newWeathersFromNetwork.observeForever(weathersFromNetwork -> {
            Executors.newSingleThreadScheduledExecutor().execute(() -> {
                // Deletes old historical data
                deleteOldData();
                OpenWeatherApp.Logger.d("Old weather deleted");
                // Insert our new weather data into OpenWeatherApp's database
                mWeatherDao.bulkInsert(weathersFromNetwork);
                OpenWeatherApp.Logger.d("New values inserted");
            });
        });
//
//
//        // As long as the repository exists, observe the network LiveData.
//        // If that LiveData changes, update the database.
//        // New weather fetch from network (by cityID or cityName)
//
//        LiveData<WeatherEntity> newWeatherFromNetwork = getCurrentWeather();
//
//        newWeatherFromNetwork.observeForever(weathersFromNetwork -> {
//            Executors.newSingleThreadScheduledExecutor().execute(() -> {
//                // Insert our new weather data into database
//                mWeatherDao.insert(weathersFromNetwork);
//                OpenWeatherApp.Logger.d("New value inserted");
//            });
//        });
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

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     */
    private synchronized void initializeData() {

        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (mInitialized) return;
        mInitialized = true;

        // This method call triggers Sunshine to create its task to synchronize weather data
        // periodically.
        mNetworkDataSource.scheduleRecurringFetchWeatherSync();

        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            if (isFetchNeeded()) { //TODO implement isFetchNeeded
                deleteOldData();
                startFetchWeatherService();
            }
        });
    }


    /**
     * Checks if at least exist one weather entry, that would be mean that exists at least one seletected and stored weather-city in the DB,
     * and the lastUpdateTime should not be older than the time of SYNC_INTERVAL ({@link NetworkDataSource})
     *
     * @return Whether a fetch is needed
     */
    private boolean isFetchNeeded() {
        long now = OpenWeatherDateUtils.getUnixTimeNowInSecs();
        int count = mWeatherDao.getCountCurrentWeathers();
        long lastUpdate = mWeatherDao.getLastUpdateTime();
        return (count > 0 && mNetworkDataSource.isSyncNeeded(now, lastUpdate));
    }

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


    //------------------

    /**
     * Network related operation
     */

    private void startFetchWeatherService() {
        mNetworkDataSource.startFetchWeatherService();
    }


    public LiveData<List<WeatherEntity>> getCurrentWeathers() {
        initializeData();
        //long today = OpenWeatherDateUtils.getNormalizedUtcSecondsForToday();
        return mWeatherDao.getCurrentWeather();
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

    public LiveData<WeatherEntity> getResponseWeatherByCityName() {
        return mNetworkDataSource.getCurrentWeatherByCityName();
    }

    public void fetchCurrentWeathersByCityIDs() {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            List<Integer> list = mWeatherDao.getAllWeathersId(); //We get the list of weather ID from the DB
            String idList = Utils.listToCommaValues(list);

            mNetworkDataSource.fetchCurrentWeathersByCitiesID(idList);
        });
    }

    //------------------

//
//    public LiveData<List<WeatherEntity>> getAllWeather(){
//
//        return mWeatherDao.getAllWeathers();
//    }

//    public List<CityEntity> getAllCities() {
//        try {
//            return new RetrieveAllCities(mCityDao).execute().get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public List<CityEntity> getCitiesByName(String text) {
//        try {
//            return new RetrieveCitiesByName(mCityDao).execute(text).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


//    /**
//     * fetchAndInsertWeather from network by cityID
//     * @param cityID
//     */
//    public void getWeatherByCityID(String cityID) {
//        Executors.newSingleThreadScheduledExecutor().execute(() -> {
//            mNetworkDataSource.fetchCurrentWeatherByCityID(cityID);
//        });
//    }
//

//
//    public void insertDummyWeather() {
//        //Remember use a separate thread when you insert elements into database
//        Executors.newSingleThreadScheduledExecutor().execute(() -> {
//            mWeatherDao.insert(new WeatherEntity(442, 123.45, 123.45, 123, "frio", "descripcion", "icon",
//                    "base", 25.45, 45, 13, 12.45, 45.12, 1, 45.12, 1, 1,
//                    1535013592, 5, 4, 45.4, "PY", 123456, 1321645, "name", 200));
//            OpenWeatherApp.Logger.d("Dummy object inserted");
//        });
//    }
//
//    public void deleteDummyWeather() {
//        //Remember use a separate thread when you insert/delete elements into database
//        Executors.newSingleThreadScheduledExecutor().execute(() -> {
//            mWeatherDao.deleteDummy();
//            OpenWeatherApp.Logger.d("Dummy object deleted");
//        });
//    }


//    private void insertBulk(WeatherEntity[] weathersFromNetwork) {
//        Executors.newSingleThreadScheduledExecutor().execute(() -> {
//            // Deletes old historical data
//            deleteOldData();
//            OpenWeatherApp.Logger.d("Old weather deleted");
//            // Insert our new weather data into OpenWeatherApp's database
//            mWeatherDao.bulkInsert(weathersFromNetwork);
//            OpenWeatherApp.Logger.d("New values inserted");
//        });
//    }


//
//    private static class RetrieveAllCities extends AsyncTask<Void, Void, List<CityEntity>> {
//        CityDao cityDao;
//
//        // only retain a weak reference to the activity
//        RetrieveAllCities(CityDao cityDao) {
//            this.cityDao = cityDao;
//        }
//
//        @Override
//        protected List<CityEntity> doInBackground(Void... voids) {
//            return cityDao.getAllCities();
//        }
//
//        @Override
//        protected void onPostExecute(List<CityEntity> cityEntities) {
//            super.onPostExecute(cityEntities);
//        }
//    }
//
//    private static class RetrieveCitiesByName extends AsyncTask<String, Void, List<CityEntity>> {
//        CityDao cityDao;
//
//        // only retain a weak reference to the activity
//        RetrieveCitiesByName(CityDao cityDao) {
//            this.cityDao = cityDao;
//        }
//
//        @Override
//        protected List<CityEntity> doInBackground(String... strings) {
//            return cityDao.getCitiesByName(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(List<CityEntity> cityEntities) {
//            super.onPostExecute(cityEntities);
//        }
//    }


}
