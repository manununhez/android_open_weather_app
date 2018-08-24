package com.openweather.challenge.openweatherapp.model;

import java.util.Arrays;

/**
 * Created by manuel on 24,August,2018
 */
public class WeathersResponse {
    private int cnt;
    public WeatherResponse[] list;

    @Override
    public String toString() {
        return "WeathersResponse{" +
                "cnt=" + cnt +
                ", list=" + Arrays.toString(list) +
                '}';
    }
}
