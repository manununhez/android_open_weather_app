package com.openweather.challenge.openweatherapp.repository;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.openweather.challenge.openweatherapp.db.dao.WeatherDao;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.model.Resource;
import com.openweather.challenge.openweatherapp.network.NetworkDataSource;
import com.openweather.challenge.openweatherapp.utils.OpenWeatherDateUtils;
import com.openweather.challenge.openweatherapp.utils.Utils;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by manuel on 21,August,2018
 */
public class AppRepository {
    private final static String TAG = AppRepository.class.getSimpleName();
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
        // Group of current weathers that corresponds to the weather of all cities saved in the DB. Updated
        // using job schedules
        LiveData<Resource<WeatherEntity[]>> newWeathersFromNetwork = responseFromCurrentWeathersByCityIDs();

        newWeathersFromNetwork.observeForever(weathersFromNetwork -> {
            if (weathersFromNetwork != null) {
                if (weathersFromNetwork.status.equals(Resource.Status.SUCCESS)) {
                    // Insert our new weather data into OpenWeatherApp's database
                    bulkInsert(weathersFromNetwork.data);

                    Log.d(TAG, "New values inserted");
                } else if (weathersFromNetwork.status.equals(Resource.Status.ERROR)) {
                    //TODO do something
                    if (weathersFromNetwork.message != null)
                        Log.d(TAG, weathersFromNetwork.message);
                    else
                        Log.d(TAG, "Status ERROR");
                }
            }

        });
    }


    public synchronized static AppRepository getInstance(WeatherDao weatherDao, NetworkDataSource networkDataSource) {
        Log.d(TAG, "Getting the repository");
        if (INSTANCE == null) {
            synchronized (AppRepository.class) {
                INSTANCE = new AppRepository(weatherDao, networkDataSource);
                Log.d(TAG, "Made new repository");
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

    //*****************************
    //  Database related operation
    //****************************

    /**
     * Checks if at least one weather entry exist, that would be mean that exists at least one selected and stored weather-city in the DB,
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
     * List of current weather entries stored in DB.
     *
     * @return {@link LiveData} to observe and retrieve the current weather
     */
    public LiveData<List<WeatherEntity>> getCurrentWeathers() {
        initializeData();
        return mWeatherDao.getCurrentWeather();
    }


    /**
     * Obtains the last dt of the weather entries in DB. This is used to determine
     * if new values should be fetched from the weather server.
     *
     * @return long dt
     */
    public long getLastUpdateTime() {
        return mWeatherDao.getLastUpdateTime();
    }

    /**
     * List of all city weathers ID available in DB.
     *
     * @return list of ID int
     */
    public List<Integer> getAllWeathersId() {
        return mWeatherDao.getAllWeathersId();
    }


    /**
     * Returns how many weathers entries are in DB.
     *
     * @return int count entries
     */
    public int getCountCurrentWeathers() {
        return mWeatherDao.getCountCurrentWeathers();
    }

    /**
     * Inserts the list of current weather IDs just fetched from the weather server.
     *
     * @param weathersFromNetwork List of updated weather entries, ready to be inserted into the DB.
     */
    private void bulkInsert(WeatherEntity[] weathersFromNetwork) {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mWeatherDao.bulkInsert(weathersFromNetwork);
        });
    }


    /**
     * Inserts an specific weather entry into DB.
     *
     * @param weatherEntity new weather entry
     */
    public void insertWeather(WeatherEntity weatherEntity) {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mWeatherDao.insert(weatherEntity);
            Log.d(TAG, "Weather object inserted: " + weatherEntity.toString());
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
        Log.d(TAG, "Old weather deleted");

    }

    /**
     * Delete an specific weather item
     *
     * @param item Weather entry
     */
    public void delete(WeatherEntity item) {
        //Remember use a separate thread when you insert/delete elements into database
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mWeatherDao.delete(item);
        });
    }


    //***********************************
    //     Network related operation
    //*********************************


    /**
     * Starts a service to fetch updated data
     */
    private void startFetchWeatherService() {
        mNetworkDataSource.startFetchWeatherService();
    }

    /**
     * Verifies if a syncronization with new data from the weather server is needed. Compare the last updated time from the DB
     * with the current time, and according to a established SYNC INTERVAL time in {@link NetworkDataSource}, determines if it is needed a sync.
     *
     * @param now        Current time
     * @param lastUpdate Last updated time
     * @return
     */
    private boolean isSyncNeeded(long now, long lastUpdate) {
        return mNetworkDataSource.isSyncNeeded(now, lastUpdate);
    }

    /**
     * Fetch And Insert Weather from network by city name
     *
     * @param cityName Name of the city
     */
    public void requestCurrentWeatherByCityName(String cityName) {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mNetworkDataSource.fetchCurrentWeatherByCityName(cityName);
        });
    }

    /**
     * Used for search weather by city name.
     *
     * @return Current weather by city name
     */
    public LiveData<Resource<WeatherEntity>> responseFromCurrentWeatherByCityName() {
        return mNetworkDataSource.responseFromCurrentWeatherByCityName();
    }


    /**
     * Retrieves the ID weather entries from DB and fetch updated data for these from
     * the weather server. Fetch And Insert Weathers from network.
     */
    public void requestCurrentWeathersByCityIDs() {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            List<Integer> list = getAllWeathersId(); //We get the list of weather ID from the DB
            String idList = Utils.listToCommaValues(list);

            mNetworkDataSource.fetchCurrentWeathersByCityIDs(idList);
        });
    }

    /**
     * Used to keep updated all the weather entries from DB every SYNC_INTERVAL established in {@link NetworkDataSource}
     *
     * @return Current weather by city Ids
     */
    private LiveData<Resource<WeatherEntity[]>> responseFromCurrentWeathersByCityIDs() {
        return mNetworkDataSource.responseFromCurrentWeathersByCityIDs();
    }

    /**
     * Fetch And Insert Weather from network by city coords.
     *
     * @param lat Latitude
     * @param lon Longitude
     */
    public void requestCurrentWeatherByCityCoords(String lat, String lon) {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mNetworkDataSource.fetchCurrentWeatherByCityCoord(lat, lon);
        });
    }


    /**
     * Used for search weather by city coordinates.
     *
     * @return Current weather by city coordinate
     */
    public LiveData<Resource<WeatherEntity>> responseFromCurrentWeatherByCityCoord() {
        return mNetworkDataSource.responseFromCurrentWeatherByCityCoord();
    }
}
