package com.openweather.challenge.openweatherapp.model;

/**
 * Created by manuel on 21,August,2018
 */
public class Main {
    public double temp;
    public double pressure;
    public int humidity;
    public double temp_min;
    public double temp_max;

    @Override
    public String toString() {
        return "Main{" +
                "temp=" + temp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", temp_min=" + temp_min +
                ", temp_max=" + temp_max +
                '}';
    }
}
