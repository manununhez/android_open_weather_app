package com.openweather.challenge.openweatherapp.ui.showweather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

class ShowWeatherPagerAdapter extends FragmentPagerAdapter {
    private final List<ShowWeatherDescriptionFragment> mShowWeatherDescriptionFragmentList;

    public ShowWeatherPagerAdapter(FragmentManager fragmentManager, List<ShowWeatherDescriptionFragment> showWeatherDescriptionFragments) {
        super(fragmentManager);
        mShowWeatherDescriptionFragmentList = showWeatherDescriptionFragments;
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