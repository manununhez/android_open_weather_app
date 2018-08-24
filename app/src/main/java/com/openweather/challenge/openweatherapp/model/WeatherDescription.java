package com.openweather.challenge.openweatherapp.model;

/**
 * Created by manuel on 23,August,2018
 */
class WeatherDescription {
    public int id;
    public String main;
    public String description;
    public String icon;

    @Override
    public String toString() {
        return "WeatherDescription{" +
                "id=" + id +
                ", main='" + main + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
