/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.entity.CityEntity;
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.model.CityResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manuel on 21,August,2018
 */
public class AppDataJsonParser {


    public static CityEntity[] loadCitiesFromAsset(Context context) throws IOException, JSONException {
        //Reading source from local file
        Gson gson = new Gson();
        List<CityEntity> cityEntityList = new ArrayList<>();
        InputStream inputStream = context.getResources().openRawResource(R.raw.city_list);

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
//        List<Message> messages = new ArrayList<Message>();
        reader.beginArray();
        while (reader.hasNext()) {
            CityResponse cityResponse = gson.fromJson(reader, CityResponse.class);
            if (!cityResponse.name.isEmpty() && !cityResponse.country.isEmpty())
                cityEntityList.add(new CityEntity(cityResponse.id, cityResponse.name, cityResponse.country));
        }
        reader.endArray();
        reader.close();
//        return messages;
//
//        int size = inputStream.available();
//
//
//        byte[] buffer = new byte[size];
//        inputStream.read(buffer);
//        inputStream.close();
//
//        String dataString = new String(buffer, "UTF-8");
        CityEntity[] cityEntities = cityEntityList.toArray(new CityEntity[0]); //casting list to array (array is faster to get values)

        return cityEntities;

    }


//    private static CityEntity[] fromJson(JSONArray jsonArray) throws JSONException {
//        CityEntity[] cityEntities = new CityEntity[jsonArray.length()];
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject jsonObject = jsonArray.getJSONObject(i);
//            int id = jsonObject.getInt("id");
//            String name = jsonObject.getString("name");
//            String country = jsonObject.getString("country");
//            double lat = jsonObject.getJSONObject("coord").getDouble("lat");
//            double lon = jsonObject.getJSONObject("coord").getDouble("lon");
//
//            cityEntities[i] = new CityEntity(id, name, country, lat, lon);
//        }
//        return cityEntities;
//    }

    public static WeatherEntity parseCurrentWeatherDataForOneLocation(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("id");
        double lat = jsonObject.getJSONObject("coord").getDouble("lat");
        double lon = jsonObject.getJSONObject("coord").getDouble("lon");
        int weather_id = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
        String weather_main = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
        String weather_description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
        String weather_icon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
        String base = jsonObject.getString("base");
        double main_temp = jsonObject.getJSONObject("main").getDouble("temp");
        int main_pressure = jsonObject.getJSONObject("main").getInt("pressure");
        int main_humidity = jsonObject.getJSONObject("main").getInt("humidity");
        double main_temp_min = jsonObject.getJSONObject("main").getDouble("temp_min");
        double main_temp_max = jsonObject.getJSONObject("main").getDouble("temp_max");
        int visibility = jsonObject.getInt("visibility");
        double wind_speed = jsonObject.getDouble("speed");
        int wind_deg = jsonObject.getInt("deg");
        int clouds = jsonObject.getJSONObject("clouds").getInt("all");
        long dt = jsonObject.getLong("dt");
        int sys_type = jsonObject.getInt("type");
        int sys_id = jsonObject.getInt("id");
        double sys_message = jsonObject.getDouble("message");
        String sys_country = jsonObject.getString("country");
        long sys_sunrise = jsonObject.getLong("sunrise");
        long sys_sunset = jsonObject.getLong("sunset");
        String name = jsonObject.getString("name");
        int cod = jsonObject.getInt("cod");

        return new WeatherEntity(id, lat, lon, weather_id, weather_main, weather_description, weather_icon, base, main_temp, main_pressure, main_humidity, main_temp_min, main_temp_max,
                visibility, wind_speed, wind_deg, clouds, dt, sys_type, sys_id, sys_message, sys_country, sys_sunrise, sys_sunset, name, cod);
    }
}
