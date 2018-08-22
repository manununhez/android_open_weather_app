/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.showweather;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.openweather.challenge.openweatherapp.OpenWeatherApp;
import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.utils.InjectorUtils;

public class ShowWeatherFragment extends Fragment {

    private ShowWeatherViewModel mViewModel;
    private View view;

    public static ShowWeatherFragment newInstance() {
        return new ShowWeatherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.show_weather_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView message = (TextView) view.findViewById(R.id.tvResults);
        Button btnDummy = (Button) view.findViewById(R.id.btnDummy);

        ShowWeatherViewModelFactory factory = InjectorUtils.provideShowWeatherViewModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(ShowWeatherViewModel.class);


        mViewModel.getAllWeathers().observe(this, weatherEntities -> {
            if (weatherEntities != null){
                //message.setText("");
                for (WeatherEntity w: weatherEntities){
                    message.setText(message.getText()+w.toString());
                }
            }
        });

        btnDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.insertDummyWeather();
            }
        });
    }

}
