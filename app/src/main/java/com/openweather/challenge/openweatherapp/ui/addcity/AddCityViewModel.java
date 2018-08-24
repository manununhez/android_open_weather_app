package com.openweather.challenge.openweatherapp.ui.addcity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.openweather.challenge.openweatherapp.AppRepository;
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;

class AddCityViewModel extends ViewModel {

    private final AppRepository mRepository;


    public AddCityViewModel(AppRepository appRepository) {
        mRepository = appRepository;
    }


    public void insertWeather(WeatherEntity weatherEntity) {
        mRepository.insertWeather(weatherEntity);
    }

    public LiveData<WeatherEntity> getResponseWeatherByName() {
        return mRepository.getResponseWeatherByCityName();
    }

    public void getWeatherByName(String text) {
        mRepository.getWeatherByCityName(text);
    }


}
