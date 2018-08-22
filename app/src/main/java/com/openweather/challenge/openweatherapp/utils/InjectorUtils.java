/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.utils;

import android.content.Context;

import com.openweather.challenge.openweatherapp.AppRepository;
import com.openweather.challenge.openweatherapp.db.AppRoomDatabase;
import com.openweather.challenge.openweatherapp.ui.addcity.AddCityViewModelFactory;

import java.util.Date;

/**
 * Provides static methods to inject the various classes needed for Sunshine
 */
public class InjectorUtils {

    public static AppRepository provideRepository(Context context) {
        AppRoomDatabase database = AppRoomDatabase.getInstance(context.getApplicationContext());

        return AppRepository.getInstance(database.cityDao());
    }

//    public static WeatherNetworkDataSource provideNetworkDataSource(Context context) {
//        // This call to provide repository is necessary if the app starts from a service - in this
//        // case the repository will not exist unless it is specifically created.
//        provideRepository(context.getApplicationContext());
//        AppExecutors executors = AppExecutors.getInstance();
//        return WeatherNetworkDataSource.getInstance(context.getApplicationContext(), executors);
//    }

    public static AddCityViewModelFactory provideAddCityViewModelFactory(Context context) {
        AppRepository repository = provideRepository(context.getApplicationContext());
        return new AddCityViewModelFactory(repository);
    }

//    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
//        SunshineRepository repository = provideRepository(context.getApplicationContext());
//        return new MainViewModelFactory(repository);
//    }

}