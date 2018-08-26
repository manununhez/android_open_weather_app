package com.openweather.challenge.openweatherapp.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Room;
import android.os.Parcel;
import android.os.Parcelable;


/*
  Created by manuel on 21,August,2018
 */

/**
 * Defines the schema of a table in {@link Room} for a single weather
 * forecast.
 */
@Entity(tableName = "weather_table")
public class WeatherEntity implements Parcelable {

    /**
     * Makes sure the id is the primary key (ensures uniqueness), is auto generated by {@link Room}.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double lat;
    private double lon;
    private int weather_id;
    private String weather_main;
    private String weather_description;
    private String weather_icon;
    private String base;
    private double main_temp;
    private double main_pressure;
    private int main_humidity;
    private double main_temp_min;
    private double main_temp_max;
    private int visibility;
    private double wind_speed;
    private double wind_deg;
    private int clouds;
    private long dt; //Unix epoch time in SECS
    private int sys_type;
    private int sys_id;
    private double sys_message;
    private String sys_country;
    private long sys_sunrise;
    private long sys_sunset;
    private String name;
    private int cod;

    /**
     * Constructor used by {@link com.openweather.challenge.openweatherapp.model.WeatherResponse} to convert the response directly from network and adapted it to
     * this entity database version {@link WeatherEntity}
     * @param id
     * @param lat
     * @param lon
     * @param weather_id
     * @param weather_main
     * @param weather_description
     * @param weather_icon
     * @param base
     * @param main_temp
     * @param main_pressure
     * @param main_humidity
     * @param main_temp_min
     * @param main_temp_max
     * @param visibility
     * @param wind_speed
     * @param wind_deg
     * @param clouds
     * @param dt
     * @param sys_type
     * @param sys_id
     * @param sys_message
     * @param sys_country
     * @param sys_sunrise
     * @param sys_sunset
     * @param name
     * @param cod
     */
    public WeatherEntity(int id, double lat, double lon, int weather_id, String weather_main, String weather_description,
                         String weather_icon, String base, double main_temp, double main_pressure, int main_humidity, double main_temp_min,
                         double main_temp_max, int visibility, double wind_speed, double wind_deg, int clouds, long dt, int sys_type, int sys_id,
                         double sys_message, String sys_country, long sys_sunrise, long sys_sunset, String name, int cod) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.weather_id = weather_id;
        this.weather_main = weather_main;
        this.weather_description = weather_description;
        this.weather_icon = weather_icon;
        this.base = base;
        this.main_temp = main_temp;
        this.main_pressure = main_pressure;
        this.main_humidity = main_humidity;
        this.main_temp_min = main_temp_min;
        this.main_temp_max = main_temp_max;
        this.visibility = visibility;
        this.wind_speed = wind_speed;
        this.wind_deg = wind_deg;
        this.clouds = clouds;
        this.dt = dt;
        this.sys_type = sys_type;
        this.sys_id = sys_id;
        this.sys_message = sys_message;
        this.sys_country = sys_country;
        this.sys_sunrise = sys_sunrise;
        this.sys_sunset = sys_sunset;
        this.name = name;
        this.cod = cod;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(int weather_id) {
        this.weather_id = weather_id;
    }

    public String getWeather_main() {
        return weather_main;
    }

    public void setWeather_main(String weather_main) {
        this.weather_main = weather_main;
    }

    public String getWeather_description() {
        return weather_description;
    }

    public void setWeather_description(String weather_description) {
        this.weather_description = weather_description;
    }

    public String getWeather_icon() {
        return weather_icon;
    }

    public void setWeather_icon(String weather_icon) {
        this.weather_icon = weather_icon;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public double getMain_temp() {
        return main_temp;
    }

    public void setMain_temp(double main_temp) {
        this.main_temp = main_temp;
    }

    public double getMain_pressure() {
        return main_pressure;
    }

    public void setMain_pressure(double main_pressure) {
        this.main_pressure = main_pressure;
    }

    public int getMain_humidity() {
        return main_humidity;
    }

    public void setMain_humidity(int main_humidity) {
        this.main_humidity = main_humidity;
    }

    public double getMain_temp_min() {
        return main_temp_min;
    }

    public void setMain_temp_min(double main_temp_min) {
        this.main_temp_min = main_temp_min;
    }

    public double getMain_temp_max() {
        return main_temp_max;
    }

    public void setMain_temp_max(double main_temp_max) {
        this.main_temp_max = main_temp_max;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public double getWind_deg() {
        return wind_deg;
    }

    public void setWind_deg(double wind_deg) {
        this.wind_deg = wind_deg;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public int getSys_type() {
        return sys_type;
    }

    public void setSys_type(int sys_type) {
        this.sys_type = sys_type;
    }

    public int getSys_id() {
        return sys_id;
    }

    public void setSys_id(int sys_id) {
        this.sys_id = sys_id;
    }

    public double getSys_message() {
        return sys_message;
    }

    public void setSys_message(double sys_message) {
        this.sys_message = sys_message;
    }

    public String getSys_country() {
        return sys_country;
    }

    public void setSys_country(String sys_country) {
        this.sys_country = sys_country;
    }

    public long getSys_sunrise() {
        return sys_sunrise;
    }

    public void setSys_sunrise(long sys_sunrise) {
        this.sys_sunrise = sys_sunrise;
    }

    public long getSys_sunset() {
        return sys_sunset;
    }

    public void setSys_sunset(long sys_sunset) {
        this.sys_sunset = sys_sunset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", weather_id=" + weather_id +
                ", weather_main='" + weather_main + '\'' +
                ", weather_description='" + weather_description + '\'' +
                ", weather_icon='" + weather_icon + '\'' +
                ", base='" + base + '\'' +
                ", main_temp=" + main_temp +
                ", main_pressure=" + main_pressure +
                ", main_humidity=" + main_humidity +
                ", main_temp_min=" + main_temp_min +
                ", main_temp_max=" + main_temp_max +
                ", visibility=" + visibility +
                ", wind_speed=" + wind_speed +
                ", wind_deg=" + wind_deg +
                ", clouds=" + clouds +
                ", dt=" + dt +
                ", sys_type=" + sys_type +
                ", sys_id=" + sys_id +
                ", sys_message=" + sys_message +
                ", sys_country='" + sys_country + '\'' +
                ", sys_sunrise=" + sys_sunrise +
                ", sys_sunset=" + sys_sunset +
                ", name='" + name + '\'' +
                ", cod=" + cod +
                '}';
    }

    /*************************
     * PARCELABLE implementation
     ************************/

    public static final Creator<WeatherEntity> CREATOR = new Creator<WeatherEntity>() {
        @Override
        public WeatherEntity createFromParcel(Parcel in) {
            return new WeatherEntity(in);
        }

        @Override
        public WeatherEntity[] newArray(int size) {
            return new WeatherEntity[size];
        }
    };

    protected WeatherEntity(Parcel in) {
        id = in.readInt();
        lat = in.readDouble();
        lon = in.readDouble();
        weather_id = in.readInt();
        weather_main = in.readString();
        weather_description = in.readString();
        weather_icon = in.readString();
        base = in.readString();
        main_temp = in.readDouble();
        main_pressure = in.readDouble();
        main_humidity = in.readInt();
        main_temp_min = in.readDouble();
        main_temp_max = in.readDouble();
        visibility = in.readInt();
        wind_speed = in.readDouble();
        wind_deg = in.readInt();
        clouds = in.readInt();
        dt = in.readLong();
        sys_type = in.readInt();
        sys_id = in.readInt();
        sys_message = in.readDouble();
        sys_country = in.readString();
        sys_sunrise = in.readLong();
        sys_sunset = in.readLong();
        name = in.readString();
        cod = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
        parcel.writeInt(weather_id);
        parcel.writeString(weather_main);
        parcel.writeString(weather_description);
        parcel.writeString(weather_icon);
        parcel.writeString(base);
        parcel.writeDouble(main_temp);
        parcel.writeDouble(main_pressure);
        parcel.writeInt(main_humidity);
        parcel.writeDouble(main_temp_min);
        parcel.writeDouble(main_temp_max);
        parcel.writeInt(visibility);
        parcel.writeDouble(wind_speed);
        parcel.writeDouble(wind_deg);
        parcel.writeInt(clouds);
        parcel.writeLong(dt);
        parcel.writeInt(sys_type);
        parcel.writeInt(sys_id);
        parcel.writeDouble(sys_message);
        parcel.writeString(sys_country);
        parcel.writeLong(sys_sunrise);
        parcel.writeLong(sys_sunset);
        parcel.writeString(name);
        parcel.writeInt(cod);
    }
}
