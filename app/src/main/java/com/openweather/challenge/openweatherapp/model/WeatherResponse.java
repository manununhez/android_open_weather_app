/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.model;

import com.openweather.challenge.openweatherapp.entity.WeatherEntity;

/**
 * Created by manuel on 23,August,2018
 */
public class WeatherResponse {
    public int id;
    public Coordinate coord;
    public WeatherDescription[] weather;
    public String base;
    public Main main;
    public int visibility;
    public Wind wind;
    public Clouds clouds;
    public long dt;
    public Sys sys;
    public String name;
    public int cod;

    /**
     * Transform a {@link WeatherResponse} object (received from network open weather map) to a {@link WeatherEntity} (format to save it in the DB)
     * @return
     */
    public WeatherEntity getWeatherEntity() {
        return new WeatherEntity(id, coord.lat, coord.lon, weather[0].id, weather[0].main, weather[0].description, weather[0].icon, base, main.temp, main.pressure, main.humidity,
                main.temp_min, main.temp_max, visibility, wind.speed, wind.deg, clouds.all, dt, sys.type, sys.id, sys.message, sys.country, sys.sunrise, sys.sunset, name, cod);
    }

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "id=" + id +
                ", coord=" + coord +
                ", weather=" + weather +
                ", base='" + base + '\'' +
                ", main=" + main +
                ", visibility=" + visibility +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", dt=" + dt +
                ", sys=" + sys +
                ", name='" + name + '\'' +
                ", cod=" + cod +
                '}';
    }
}
