package com.openweather.challenge.openweatherapp.model;

import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;

import java.util.Arrays;

/**
 * Created by manuel on 23,August,2018
 */
public class WeatherResponse {
    public Coordinate coord;
    public WeatherDescription[] weather;
    public String base;
    public Main main;
    public int visibility;
    public Wind wind;
    public Clouds clouds;
    public long dt;
    public Sys sys;
    public int id;
    public String name;
    public int cod;
    public String message;


    /**
     * Transform a {@link WeatherResponse} object (received from network open weather map) to a {@link WeatherEntity} (format to save it in the DB)
     *
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
                ", weather=" + Arrays.toString(weather) +
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
