/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.addcity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.openweather.challenge.openweatherapp.AppRepository;
import com.openweather.challenge.openweatherapp.entity.CityEntity;
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;

import java.util.List;

public class AddCityViewModel extends ViewModel {

    private final AppRepository mRepository;


    public AddCityViewModel(AppRepository appRepository) {
        mRepository = appRepository;
    }


    public List<CityEntity> getAllCities() {
        return mRepository.getAllCities();
    }

    public void insertWeather(WeatherEntity weatherEntity) {
        mRepository.insertWeather(weatherEntity);
    }

    public LiveData<WeatherEntity> getResponseWeatherByName() {
        return mRepository.getResponseWeatherByCityName();
    }

    public void getWeatherByName(String text){
        mRepository.fetchCurrentWeatherByCityName(text);
    }


}
