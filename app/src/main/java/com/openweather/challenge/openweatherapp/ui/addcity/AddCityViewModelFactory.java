package com.openweather.challenge.openweatherapp.ui.addcity;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.openweather.challenge.openweatherapp.AppRepository;

/**
 * Created by manuel on 21,August,2018
 */

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link AppRepository} and an ID for the current {@link com.openweather.challenge.openweatherapp.entity.CityEntity}
 */
public class AddCityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppRepository mRepository;

    public AddCityViewModelFactory(AppRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddCityViewModel(mRepository);
    }
}
