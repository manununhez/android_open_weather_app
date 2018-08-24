package com.openweather.challenge.openweatherapp.ui.showweather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class WeatherPagerAdapter extends FragmentPagerAdapter {
    private List<WeatherDescriptionFragment> mWeatherDescriptionFragmentList;

    public WeatherPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mWeatherDescriptionFragmentList = new ArrayList<>();
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return mWeatherDescriptionFragmentList.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return mWeatherDescriptionFragmentList.get(position);
    }


    //Updates RecyclerView
    public void addItems(List<WeatherDescriptionFragment> weatherDescriptionFragment) {
        mWeatherDescriptionFragmentList.clear();
        mWeatherDescriptionFragmentList = weatherDescriptionFragment;
        notifyDataSetChanged();
    }

}