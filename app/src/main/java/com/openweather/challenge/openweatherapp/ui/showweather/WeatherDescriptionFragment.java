/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.showweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;

import java.util.Objects;

/**
 * Created by manuel on 22,August,2018
 */
public class WeatherDescriptionFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private WeatherEntity weatherEntity;

    // newInstance constructor for creating fragment with arguments
    public static WeatherDescriptionFragment newInstance(WeatherEntity weatherEntity) {
        WeatherDescriptionFragment weatherDescriptionFragment = new WeatherDescriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable("weather",weatherEntity);
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
        View view = inflater.inflate(R.layout.fragment_weather_description, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        tvLabel.setText(weatherEntity.getName() + " , " + weatherEntity.getSys_country());
        return view;
    }
}
