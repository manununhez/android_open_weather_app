/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.showweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.utils.OpenWeatherDateUtils;
import com.openweather.challenge.openweatherapp.utils.OpenWeatherUtils;

import java.util.Objects;

/**
 * Created by manuel on 22,August,2018
 */
public class WeatherDescriptionFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private WeatherEntity weatherEntity;
    private View view;

    // newInstance constructor for creating fragment with arguments
    public static WeatherDescriptionFragment newInstance(WeatherEntity weatherEntity) {
        WeatherDescriptionFragment weatherDescriptionFragment = new WeatherDescriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable("weather", weatherEntity);
        weatherDescriptionFragment.setArguments(args);
        return weatherDescriptionFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherEntity = (WeatherEntity) (Objects.requireNonNull(getArguments()).getParcelable("weather"));
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weather_description, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView tvCityName = (TextView) view.findViewById(R.id.tvCityName);
        TextView tvCurrentTemp = (TextView) view.findViewById(R.id.tvCurrentTemp);
        TextView tvWeatherMain = (TextView) view.findViewById(R.id.tvWeatherMain);
        TextView tvMinMaxTemp = (TextView) view.findViewById(R.id.tvMinMaxTemp);
        TextView tvWeatherDescription = (TextView) view.findViewById(R.id.tvWeatherDescription);
        TextView tvWindSpeed = (TextView) view.findViewById(R.id.tvWindSpeed);
        TextView tvHumidity = (TextView) view.findViewById(R.id.tvHumidity);
        TextView tvSunrise = (TextView) view.findViewById(R.id.tvSunrise);
        TextView tvSunset = (TextView) view.findViewById(R.id.tvSunset);
        TextView tvLastUpdate = (TextView) view.findViewById(R.id.tvLastUpdate);
        ImageView ivWeatherCondition = (ImageView) view.findViewById(R.id.ivWeatherCondition);

        int weatherImageId = OpenWeatherUtils.geResourceIdForWeatherCondition(weatherEntity.getWeather_id());


        tvCityName.setText(weatherEntity.getName() + " , " + weatherEntity.getSys_country());
        tvCurrentTemp.setText(OpenWeatherUtils.formatTemperature(getActivity(), weatherEntity.getMain_temp()));
        tvWeatherMain.setText(weatherEntity.getWeather_main());
        tvWeatherDescription.setText(weatherEntity.getWeather_description());
        tvMinMaxTemp.setText(OpenWeatherUtils.formatTemperature(getActivity(),weatherEntity.getMain_temp_max()) + " / " +
                OpenWeatherUtils.formatTemperature(getActivity(),weatherEntity.getMain_temp_min()));

        tvWindSpeed.setText(OpenWeatherUtils.getFormattedWind(getActivity(), weatherEntity.getWind_speed(), weatherEntity.getWind_deg()));

        tvHumidity.setText(weatherEntity.getMain_humidity()+"%");
        tvSunrise.setText(OpenWeatherDateUtils.getHourFromLongTimeSeconds(weatherEntity.getSys_sunrise()));
        tvSunset.setText(OpenWeatherDateUtils.getHourFromLongTimeSeconds(weatherEntity.getSys_sunset()));

        tvLastUpdate.setText(OpenWeatherDateUtils.getFriendlyDateString(weatherEntity.getDt())); //Update last update value


        ivWeatherCondition.setImageResource(weatherImageId);

    }
}
