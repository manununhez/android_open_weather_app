/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.showweather;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.openweather.challenge.openweatherapp.AppRepository;
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;

import java.util.List;

public class ShowWeatherViewModel extends ViewModel {
    // City the user is looking at
//    private final LiveData<List<WeatherEntity>> mWeather;

    private final AppRepository mRepository;


    public ShowWeatherViewModel(AppRepository appRepository) {
        mRepository = appRepository;
    }


//
//    public void insertDummyWeather() {
//        mRepository.insertDummyWeather();
//    }
//
//    public void deleteDummyWeather() {
//        mRepository.deleteDummyWeather();
//    }

    public LiveData<List<WeatherEntity>> getCurrentWeathers(){
        return mRepository.getCurrentWeathers();

    }
}
