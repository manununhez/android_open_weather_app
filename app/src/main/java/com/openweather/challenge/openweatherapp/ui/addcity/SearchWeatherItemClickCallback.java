package com.openweather.challenge.openweatherapp.ui.addcity;

import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;

public interface SearchWeatherItemClickCallback {
    void onClick(WeatherEntity weatherEntity);

    boolean onLongClick(WeatherEntity weatherEntity);
}
