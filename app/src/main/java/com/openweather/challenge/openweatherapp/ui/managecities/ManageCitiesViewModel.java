package com.openweather.challenge.openweatherapp.ui.managecities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.openweather.challenge.openweatherapp.repository.AppRepository;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;

import java.util.List;

class ManageCitiesViewModel extends ViewModel {

    private final AppRepository mRepository;


    public ManageCitiesViewModel(AppRepository appRepository) {
        mRepository = appRepository;
    }


    public LiveData<List<WeatherEntity>> getCurrentWeathers(){
        return mRepository.getCurrentWeathers();

    }

    public void deleteWeather(WeatherEntity item) {
        mRepository.delete(item);
    }
}
