package com.openweather.challenge.openweatherapp.ui.showweather;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.databinding.ShowWeatherDescriptionFragmentBinding;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.utils.OpenWeatherDateUtils;
import com.openweather.challenge.openweatherapp.utils.OpenWeatherUtils;

import java.util.Objects;

/**
 * Created by manuel on 22,August,2018
 */
public class ShowWeatherDescriptionFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private WeatherEntity weatherEntity;
    private View rootView;
    private static final String WEATHER = "weather";
private ShowWeatherDescriptionFragmentBinding mBinding;

    // newInstance constructor for creating fragment with arguments
    public static ShowWeatherDescriptionFragment newInstance(WeatherEntity weatherEntity) {
        ShowWeatherDescriptionFragment showWeatherDescriptionFragment = new ShowWeatherDescriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(WEATHER, weatherEntity);
        showWeatherDescriptionFragment.setArguments(args);
        return showWeatherDescriptionFragment;
    }


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherEntity = Objects.requireNonNull(getArguments()).getParcelable(WEATHER);
    }

    // Inflate the rootView for the fragment based on layout XML
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.show_weather_description_fragment, container, false);
        mBinding.setWeatherEntity(weatherEntity);
        return mBinding.getRoot();
    }

}
