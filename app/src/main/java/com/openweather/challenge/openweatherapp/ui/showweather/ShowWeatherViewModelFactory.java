package com.openweather.challenge.openweatherapp.ui.showweather;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.openweather.challenge.openweatherapp.repository.AppRepository;

/**
 * Created by manuel on 21,August,2018
 */

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link AppRepository}
 */
public class ShowWeatherViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppRepository mRepository;

    public ShowWeatherViewModelFactory(AppRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ShowWeatherViewModel(mRepository);
    }
}
