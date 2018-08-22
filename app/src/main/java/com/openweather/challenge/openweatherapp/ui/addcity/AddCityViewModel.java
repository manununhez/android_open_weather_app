/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.addcity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.openweather.challenge.openweatherapp.AppRepository;
import com.openweather.challenge.openweatherapp.entity.CityEntity;

import java.util.List;

public class AddCityViewModel extends ViewModel {
    // City the user is looking at
//    private final LiveData<List<CityEntity>> mCityEntity;
    private final List<CityEntity> mCityEntity;

    private final AppRepository mRepository;


    public AddCityViewModel(AppRepository appRepository) {
        mRepository = appRepository;
        mCityEntity = mRepository.getAllCities();
    }

//    public LiveData<List<CityEntity>> getAllCities() {
//        return mCityEntity;
//    }

    public List<CityEntity> getAllCities() {
        return mCityEntity;
    }
}
