package com.openweather.challenge.openweatherapp.ui.addcity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.model.Resource;
import com.openweather.challenge.openweatherapp.repository.AppRepository;

import java.util.List;

class AddCityViewModel extends ViewModel {

    private final AppRepository mRepository;


    public AddCityViewModel(AppRepository appRepository) {
        mRepository = appRepository;
    }


    public void insertWeather(WeatherEntity weatherEntity) {
        mRepository.insertWeather(weatherEntity);
    }

    public void requestCurrentWeatherByCityName(String text) {
        mRepository.requestCurrentWeatherByCityName(text);
    }

    public LiveData<Resource<WeatherEntity>> responseFromCurrentWeatherByCityName() {
        return mRepository.responseFromCurrentWeatherByCityName();
    }

    public void requestCurrentWeatherByCityCoords(String lat, String lon) {
        mRepository.requestCurrentWeatherByCityCoords(lat, lon);
    }

    public LiveData<Resource<WeatherEntity>> responseFromCurrentWeatherByCityCoord() {
        return mRepository.responseFromCurrentWeatherByCityCoord();
    }


     //     Used by {@link AddCityViewModelTest}
    public WeatherEntity getWeatherById(int id) {
        return mRepository.getWeatherById(id);
    }


    //     Used by {@link AddCityViewModelTest}
    public LiveData<List<WeatherEntity>> getCurrentWeathers(){
        return mRepository.getCurrentWeathers();

    }


}
