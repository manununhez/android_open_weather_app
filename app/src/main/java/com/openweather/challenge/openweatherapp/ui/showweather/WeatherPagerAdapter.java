/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.showweather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class WeatherPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;
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

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

    public void addItems(List<WeatherDescriptionFragment> weatherDescriptionFragment){
        mWeatherDescriptionFragmentList.clear();
        mWeatherDescriptionFragmentList = weatherDescriptionFragment;
        notifyDataSetChanged();
    }

}