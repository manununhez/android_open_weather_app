package com.openweather.challenge.openweatherapp.ui.addcity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.openweather.challenge.openweatherapp.repository.AppRepository;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;

class AddCityViewModel extends ViewModel {

    private final AppRepository mRepository;


    public AddCityViewModel(AppRepository appRepository) {
        mRepository = appRepository;
    }


    public void insertWeather(WeatherEntity weatherEntity) {
        mRepository.insertWeather(weatherEntity);
    }

    public LiveData<WeatherEntity> responseFromCurrentWeatherByCityName() {
        return mRepository.responseFromCurrentWeatherByCityName();
    }

    public void fetchCurrentWeatherByCityName(String text) {
        mRepository.fetchCurrentWeatherByCityName(text);
    }

    public void fetchCurrentWeatherByCityCoords(String lat, String lon) {
        mRepository.fetchCurrentWeatherByCityCoords(lat, lon);
    }


    public LiveData<WeatherEntity> responseFromCurrentWeatherByCityCoord() {
        return mRepository.responseFromCurrentWeatherByCityCoord();
    }
}
