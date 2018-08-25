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

    public LiveData<WeatherEntity> responseWeatherByCityName() {
        return mRepository.responseWeatherByCityName();
    }

    public void getWeatherByCityName(String text) {
        mRepository.getWeatherByCityName(text);
    }


}
