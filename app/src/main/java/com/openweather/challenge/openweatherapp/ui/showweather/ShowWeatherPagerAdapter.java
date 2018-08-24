package com.openweather.challenge.openweatherapp.ui.showweather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class ShowWeatherPagerAdapter extends FragmentPagerAdapter {
    private List<ShowWeatherDescriptionFragment> mShowWeatherDescriptionFragmentList;

    public ShowWeatherPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mShowWeatherDescriptionFragmentList = new ArrayList<>();
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return mShowWeatherDescriptionFragmentList.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return mShowWeatherDescriptionFragmentList.get(position);
    }


    //Updates RecyclerView
    public void addItems(List<ShowWeatherDescriptionFragment> showWeatherDescriptionFragment) {
        mShowWeatherDescriptionFragmentList.clear();
        mShowWeatherDescriptionFragmentList.addAll(showWeatherDescriptionFragment);
        notifyDataSetChanged();
    }

}