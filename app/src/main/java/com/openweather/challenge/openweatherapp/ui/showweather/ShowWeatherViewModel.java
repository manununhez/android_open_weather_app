package com.openweather.challenge.openweatherapp.ui.showweather;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.openweather.challenge.openweatherapp.repository.AppRepository;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;

import java.util.List;

class ShowWeatherViewModel extends ViewModel {
    private final AppRepository mRepository;


    public ShowWeatherViewModel(AppRepository appRepository) {
        mRepository = appRepository;
    }


    public LiveData<List<WeatherEntity>> getCurrentWeathers(){
        return mRepository.getCurrentWeathers();

    }

    public int getCountCurrentWeathers() {
        return mRepository.getCountCurrentWeathers();
    }
}
