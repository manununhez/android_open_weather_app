package com.openweather.challenge.openweatherapp.model;

import com.openweather.challenge.openweatherapp.entity.WeatherEntity;

/**
 * Created by manuel on 23,August,2018
 */
public class WeatherResponse {
    private Coordinate coord;
    private WeatherDescription[] weather;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private long dt;
    private Sys sys;
    private int id;
    private String name;
    private int cod;


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
