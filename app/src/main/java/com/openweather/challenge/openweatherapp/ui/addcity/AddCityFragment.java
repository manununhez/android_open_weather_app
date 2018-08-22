/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.addcity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.entity.CityEntity;
import com.openweather.challenge.openweatherapp.utils.InjectorUtils;

import java.util.List;

public class AddCityFragment extends Fragment {

    private AddCityViewModel mViewModel;
    private Button btnTest;
    private View view;

    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_city_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get the ViewModel from the factory
        AddCityViewModelFactory factory = InjectorUtils.provideAddCityViewModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(AddCityViewModel.class);


//        mViewModel.getAllCities().observe(this, cityEntities -> {
//            if (cityEntities != null) OpenWeatherApp.Logger.d(cityEntities.toString());
//        });

        btnTest = (Button) view.findViewById(R.id.btnTest);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Remember use a separate thread when you insert elements into database

                List<CityEntity> cityEntities = mViewModel.getAllCities();

            }
        });


    }


}
