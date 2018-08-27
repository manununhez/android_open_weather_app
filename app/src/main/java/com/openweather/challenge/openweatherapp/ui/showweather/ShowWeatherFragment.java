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
import com.openweather.challenge.openweatherapp.ui.addcity.AddCityActivity;
import com.openweather.challenge.openweatherapp.ui.managecities.ManageCitiesActivity;
import com.openweather.challenge.openweatherapp.utils.InjectorUtils;
import com.openweather.challenge.openweatherapp.utils.ZoomOutPageTransformer;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class ShowWeatherFragment extends Fragment {

    private ShowWeatherViewModel mViewModel;
    private View rootView;
    private ShowWeatherPagerAdapter pagerAdapter;

    public static ShowWeatherFragment newInstance() {
        return new ShowWeatherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.show_weather_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true); //search menu functionality

        initializeViewPager();

//        tvLastUpdate = rootView.findViewById(R.id.tvLastUpdate);

        ShowWeatherViewModelFactory factory = InjectorUtils.provideShowWeatherViewModelFactory(Objects.requireNonNull(getActivity()).getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(ShowWeatherViewModel.class);


        /*
          if there are not weather to display, we go to addCity directly
         */
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            if (mViewModel.getCountCurrentWeathers() == 0)
                goToAddCityActivity();
        });


        /*
          Update viewpager witch the current weathers stored
         */
        observeCurrentWeathers();

    }

    private void observeCurrentWeathers(){
        mViewModel.getCurrentWeathers().observe(this, weatherEntities -> {
            if (weatherEntities != null && !weatherEntities.isEmpty()) {

                List<ShowWeatherDescriptionFragment> showWeatherDescriptionFragments = new ArrayList<>();
                int i = 0;
                for (WeatherEntity weatherEntity : weatherEntities) {

                    showWeatherDescriptionFragments.add(ShowWeatherDescriptionFragment.newInstance(weatherEntity));
                }

                pagerAdapter.addItems(showWeatherDescriptionFragments); //add items and refresh viewpager
//                tvLastUpdate.setText(OpenWeatherDateUtils.getFriendlyDateString(weatherEntities.get(0).getDt())); //Update last update value
            }
        });
    }


    private void initializeViewPager() {
        DotsIndicator dotsIndicator = rootView.findViewById(R.id.dots_indicator);
        ViewPager viewPager = rootView.findViewById(R.id.view_pager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        List<ShowWeatherDescriptionFragment> showWeatherDescriptionFragments = new ArrayList<>();

        setViewPager(viewPager, showWeatherDescriptionFragments);
        dotsIndicator.setViewPager(viewPager);

    }

    private void setViewPager(ViewPager viewPager, List<ShowWeatherDescriptionFragment> showWeatherDescriptionFragments) {
        pagerAdapter = new ShowWeatherPagerAdapter(getFragmentManager(), showWeatherDescriptionFragments);
        viewPager.setAdapter(pagerAdapter);
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
                goToManageCitiesActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToManageCitiesActivity() {
        startActivity(new Intent(getActivity(), ManageCitiesActivity.class));
    }

    private void goToAddCityActivity() {
        startActivity(new Intent(getActivity(), AddCityActivity.class));
    }
}

