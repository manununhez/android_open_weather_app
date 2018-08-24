package com.openweather.challenge.openweatherapp.model;

/**
 * Created by manuel on 21,August,2018
 */
public class Sys {
    public int type;
    public int id;
    public double message;
    public String country;
    public long sunrise;
    public long sunset;

    @Override
    public String toString() {
        return "Sys{" +
                "type=" + type +
                ", id=" + id +
                ", message=" + message +
                ", country='" + country + '\'' +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                '}';
    }
}
