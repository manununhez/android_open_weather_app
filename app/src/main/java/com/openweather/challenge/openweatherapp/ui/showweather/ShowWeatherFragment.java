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

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.ui.managecities.ManageCitiesActivity;
import com.openweather.challenge.openweatherapp.utils.InjectorUtils;
import com.openweather.challenge.openweatherapp.utils.ZoomOutPageTransformer;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowWeatherFragment extends Fragment {

    private ShowWeatherViewModel mViewModel;
    private View view;
    private ShowWeatherPagerAdapter pagerAdapter;
//    private TextView tvLastUpdate;

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

        viewPagerSettings();

//        tvLastUpdate = view.findViewById(R.id.tvLastUpdate);

        ShowWeatherViewModelFactory factory = InjectorUtils.provideShowWeatherViewModelFactory(Objects.requireNonNull(getActivity()).getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(ShowWeatherViewModel.class);


        mViewModel.getCurrentWeathers().observe(this, weatherEntities -> {
            if (weatherEntities != null && !weatherEntities.isEmpty()) {

                List<ShowWeatherDescriptionFragment> showWeatherDescriptionFragments = new ArrayList<>();
                int i = 0;
                for (WeatherEntity weatherEntity : weatherEntities) {

                    showWeatherDescriptionFragments.add(ShowWeatherDescriptionFragment.newInstance(weatherEntity));
                }

                pagerAdapter.addItems(showWeatherDescriptionFragments);


//                tvLastUpdate.setText(OpenWeatherDateUtils.getFriendlyDateString(weatherEntities.get(0).getDt())); //Update last update value
            }
        });

    }


    private void viewPagerSettings() {
        DotsIndicator dotsIndicator = view.findViewById(R.id.dots_indicator);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());


        pagerAdapter = new ShowWeatherPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        dotsIndicator.setViewPager(viewPager);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

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

