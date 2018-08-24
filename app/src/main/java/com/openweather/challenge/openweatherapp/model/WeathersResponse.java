/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.model;

import java.util.Arrays;

/**
 * Created by manuel on 24,August,2018
 */
public class WeathersResponse {
    public int cnt;
    public WeatherResponse[] list;

    @Override
    public String toString() {
        return "WeathersResponse{" +
                "cnt=" + cnt +
                ", list=" + Arrays.toString(list) +
                '}';
    }
}
