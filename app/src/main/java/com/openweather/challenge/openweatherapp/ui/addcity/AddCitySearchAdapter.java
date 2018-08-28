package com.openweather.challenge.openweatherapp.ui.addcity;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.databinding.WeatherItemSBinding;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;

import java.util.List;

public class AddCitySearchAdapter extends RecyclerView.Adapter<AddCitySearchAdapter.WeatherViewHolder> {

    private final List<WeatherEntity> weatherEntityList;
    private final SearchWeatherItemClickCallback mWeatherItemClickCallback;

    public AddCitySearchAdapter(List<WeatherEntity> arrayList, SearchWeatherItemClickCallback searchWeatherItemClickCallback) {
        weatherEntityList = arrayList;
        mWeatherItemClickCallback = searchWeatherItemClickCallback;

    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        WeatherItemSBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.weather_item_s,
                        parent, false);
        binding.setOnClickCallback(mWeatherItemClickCallback);
        return new WeatherViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder weatherViewHolder, int position) {
        weatherViewHolder.binding.setWeatherEntity(weatherEntityList.get(position));
    }

    @Override
    public int getItemCount() {
        return weatherEntityList.size();
    }

    public void setItemList(List<WeatherEntity> weatherEntities) {
        weatherEntityList.clear();
        weatherEntityList.addAll(weatherEntities);

        notifyDataSetChanged();
    }


    public class WeatherViewHolder extends RecyclerView.ViewHolder {

        final WeatherItemSBinding binding;

        WeatherViewHolder(WeatherItemSBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
