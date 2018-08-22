/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.showweather;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.openweather.challenge.openweatherapp.AppRepository;
import com.openweather.challenge.openweatherapp.ui.addcity.AddCityViewModel;

/**
 * Created by manuel on 21,August,2018
 */

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link AppRepository} and an ID for the current {@link com.openweather.challenge.openweatherapp.entity.CityEntity}
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
