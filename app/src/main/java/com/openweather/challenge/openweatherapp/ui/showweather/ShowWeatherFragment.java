/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.showweather;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.ui.managecities.ManageCitiesActivity;
import com.openweather.challenge.openweatherapp.utils.InjectorUtils;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowWeatherFragment extends Fragment {

    private ShowWeatherViewModel mViewModel;
    private View view;
    private WeatherPagerAdapter pagerAdapter;

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

        setHasOptionsMenu(true); //search menu functionality


        TextView message = view.findViewById(R.id.tvResults);
        Button btnDummy = view.findViewById(R.id.btnDummy);
        Button btnDummyDelete = view.findViewById(R.id.btnDummyDelete);

        viewPagerSettings();

        ShowWeatherViewModelFactory factory = InjectorUtils.provideShowWeatherViewModelFactory(Objects.requireNonNull(getActivity()).getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(ShowWeatherViewModel.class);


        mViewModel.getAllWeathers().observe(this, weatherEntities -> {
            if (weatherEntities != null) {
                //message.setText("");
                List<WeatherDescriptionFragment> weatherDescriptionFragments = new ArrayList<>();
                int i = 0;
                for (WeatherEntity w : weatherEntities) {
//                    message.setText(message.getText() + w.toString());


                    weatherDescriptionFragments.add(WeatherDescriptionFragment.newInstance(i++, "Page # "+i++));
                }

                pagerAdapter.addItems(weatherDescriptionFragments);
                //pagerAdapter.notifyDataSetChanged();

            }
        });

        btnDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.insertDummyWeather();
            }
        });

        btnDummyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.deleteDummyWeather();
            }
        });
    }


    private void viewPagerSettings() {
        DotsIndicator dotsIndicator = view.findViewById(R.id.dots_indicator);
        ViewPager viewPager = view.findViewById(R.id.view_pager);

        pagerAdapter = new WeatherPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        dotsIndicator.setViewPager(viewPager);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_add_city).setVisible(false);
        menu.findItem(R.id.action_manage_city).setVisible(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_manage_city:
                startActivity(new Intent(getActivity(), ManageCitiesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

