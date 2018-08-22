/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp;

import android.os.AsyncTask;

import com.openweather.challenge.openweatherapp.db.dao.CityDao;
import com.openweather.challenge.openweatherapp.entity.CityEntity;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by manuel on 21,August,2018
 */
public class AppRepository {
    // For Singleton instantiation
    private static AppRepository INSTANCE;
    private final CityDao mCityDao;

    public AppRepository(CityDao cityDao) {
        mCityDao = cityDao;
    }

    public synchronized static AppRepository getInstance(
            CityDao cityDao) {
        OpenWeatherApp.Logger.d("Getting the repository");
        if (INSTANCE == null) {
            synchronized (AppRepository.class) {
                INSTANCE = new AppRepository(cityDao);
                OpenWeatherApp.Logger.d("Made new repository");
            }
        }
        return INSTANCE;
    }

    public List<CityEntity> getAllCities() {
        try {
            return new RetrieveTask(mCityDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //return mCityDao.getAllCities();
        return null;
    }

//    public LiveData<List<CityEntity>> getAllCities() {
//        return mCityDao.getAllCities();
//    }

    private static class RetrieveTask extends AsyncTask<Void, Void, List<CityEntity>> {

        //        private WeakReference<AddCityFragment> activityReference;
//        private AddCityViewModel mViewModel;
        CityDao cityDao;

        // only retain a weak reference to the activity
        RetrieveTask(CityDao cityDao) {
           this.cityDao = cityDao;
        }

        @Override
        protected List<CityEntity> doInBackground(Void... voids) {
            return cityDao.getAllCities();
        }

        @Override
        protected void onPostExecute(List<CityEntity> cityEntities) {

            for (CityEntity c : cityEntities) {
                OpenWeatherApp.Logger.d(c.toString());
            }
        }

    }
}
