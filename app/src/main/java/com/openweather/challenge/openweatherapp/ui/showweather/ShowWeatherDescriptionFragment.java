package com.openweather.challenge.openweatherapp.ui.showweather;

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
//    private static String WEATHER_SAVED = "weather_saved";

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
        rootView = inflater.inflate(R.layout.show_weather_description_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView tvCityName = rootView.findViewById(R.id.tvCityName);
        TextView tvCurrentTemp = rootView.findViewById(R.id.tvCurrentTemp);
        TextView tvWeatherMain = rootView.findViewById(R.id.tvWeatherMain);
        TextView tvMinMaxTemp = rootView.findViewById(R.id.tvMinMaxTemp);
        TextView tvWeatherDescription = rootView.findViewById(R.id.tvWeatherDescription);
        TextView tvWindSpeed = rootView.findViewById(R.id.tvWindSpeed);
        TextView tvHumidity = rootView.findViewById(R.id.tvHumidity);
        TextView tvSunrise = rootView.findViewById(R.id.tvSunrise);
        TextView tvSunset = rootView.findViewById(R.id.tvSunset);
        TextView tvLastUpdate = rootView.findViewById(R.id.tvLastUpdate);
        ImageView ivWeatherCondition = rootView.findViewById(R.id.ivWeatherCondition);

        int weatherImageId = OpenWeatherUtils.geResourceIdForWeatherCondition(weatherEntity.getWeather_id());


        tvCityName.setText(Objects.requireNonNull(getActivity()).getString(R.string.full_city_name, weatherEntity.getName(), weatherEntity.getSys_country()));
        tvCurrentTemp.setText(OpenWeatherUtils.formatTemperature(Objects.requireNonNull(getActivity()), weatherEntity.getMain_temp()));
        tvWeatherMain.setText(weatherEntity.getWeather_main());
        tvWeatherDescription.setText(weatherEntity.getWeather_description());
        tvMinMaxTemp.setText(getActivity().getString(R.string.format_min_max_temperature,OpenWeatherUtils.formatTemperature(getActivity(), weatherEntity.getMain_temp_max()),
                OpenWeatherUtils.formatTemperature(getActivity(), weatherEntity.getMain_temp_min())));

        tvWindSpeed.setText(OpenWeatherUtils.getFormattedWind(getActivity(), weatherEntity.getWind_speed(), weatherEntity.getWind_deg()));

        tvHumidity.setText(getActivity().getString(R.string.format_humidity,weatherEntity.getMain_humidity()));
        tvSunrise.setText(OpenWeatherDateUtils.getHourFromLongTimeSeconds(weatherEntity.getSys_sunrise()));
        tvSunset.setText(OpenWeatherDateUtils.getHourFromLongTimeSeconds(weatherEntity.getSys_sunset()));

        tvLastUpdate.setText(OpenWeatherDateUtils.getTimeAgo(weatherEntity.getDt())); //Update last update value


        ivWeatherCondition.setImageResource(weatherImageId);

    }
}
