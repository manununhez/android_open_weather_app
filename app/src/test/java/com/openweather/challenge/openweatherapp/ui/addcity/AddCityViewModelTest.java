/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.addcity;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.model.Resource;
import com.openweather.challenge.openweatherapp.network.NetworkDataSource;
import com.openweather.challenge.openweatherapp.repository.AppRepository;
import com.openweather.challenge.openweatherapp.ui.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



 // Created by manuel on 27,August,2018

/**
 * Unit tests for the implementation of {@link AddCityViewModel}
 */
public class AddCityViewModelTest {
//    // Executes each task synchronously using Architecture Components.
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    Observer<List<WeatherEntity>> weatherEntityLiveData;

    @Mock
    AppRepository mAppRepository;

    int id = 5;
    WeatherEntity mWeatherEntity = new WeatherEntity(id, 35.5, 25.8, 5, "Clouds", "A lot of clouds", "5cn", "base",
            25.5, 45.2, 75, 25.45, 36.45, 17, 25.9, 47.8, 18, 123456, 14, 15,
            45.5, "PY", 123456, 123456, "Asuncion", 200);

    private AddCityViewModel mAddCityViewModel;

    @Before
    public void setupAddCityViewModel() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mAddCityViewModel = new AddCityViewModel(mAppRepository);

        setupContext();
    }

    private void setupContext(){
        when(mAppRepository.getWeatherById(id)).thenReturn(mWeatherEntity);

        //mAddCityViewModel.getCurrentWeathers().removeObservers(TestUtils.TEST_OBSERVER);

    }

    @Test
    public void insertWeather() {

        //When a weather is inserted into DB
        mAddCityViewModel.insertWeather(mWeatherEntity);

        //Then the repository is called
        verify(mAppRepository, times(1)).insertWeather(any(WeatherEntity.class));

        //And data loaded
        assertEquals(mAddCityViewModel.getWeatherById(id).getId(), id);

        //TODO complete this
        // Then the event is triggered
        // mAddCityViewModel.getCurrentWeathers().observe(TestUtils.TEST_OBSERVER, weatherEntityLiveData);
        //verify(weatherEntityLiveData).onChanged(null);


    }


}