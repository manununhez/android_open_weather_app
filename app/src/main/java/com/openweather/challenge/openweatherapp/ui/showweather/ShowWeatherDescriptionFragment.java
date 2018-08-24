package com.openweather.challenge.openweatherapp.ui.showweather;

import android.os.Bundle;
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
    private View view;
    private static String WEATHER = "weather";
//    private static String WEATHER_SAVED = "weather_saved";

    // newInstance constructor for creating fragment with arguments
    public static ShowWeatherDescriptionFragment newInstance(WeatherEntity weatherEntity) {
        ShowWeatherDescriptionFragment showWeatherDescriptionFragment = new ShowWeatherDescriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(WEATHER, weatherEntity);
        showWeatherDescriptionFragment.setArguments(args);
        return showWeatherDescriptionFragment;
    }


//    @Override
//    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
//        // Save the user's current game state
//        savedInstanceState.putParcelable(WEATHER_SAVED, weatherEntity);
//
//        // Always call the superclass so it can save the view hierarchy state
//        super.onSaveInstanceState(savedInstanceState);
//    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherEntity = Objects.requireNonNull(getArguments()).getParcelable(WEATHER);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_weather_description, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//
//        // Check whether we're recreating a previously destroyed instance
//        if (savedInstanceState != null) {
//            // Restore value of members from saved state
//            weatherEntity = savedInstanceState.getParcelable(WEATHER_SAVED);
//        }

        TextView tvCityName = view.findViewById(R.id.tvCityName);
        TextView tvCurrentTemp = view.findViewById(R.id.tvCurrentTemp);
        TextView tvWeatherMain = view.findViewById(R.id.tvWeatherMain);
        TextView tvMinMaxTemp = view.findViewById(R.id.tvMinMaxTemp);
        TextView tvWeatherDescription = view.findViewById(R.id.tvWeatherDescription);
        TextView tvWindSpeed = view.findViewById(R.id.tvWindSpeed);
        TextView tvHumidity = view.findViewById(R.id.tvHumidity);
        TextView tvSunrise = view.findViewById(R.id.tvSunrise);
        TextView tvSunset = view.findViewById(R.id.tvSunset);
        TextView tvLastUpdate = view.findViewById(R.id.tvLastUpdate);
        ImageView ivWeatherCondition = view.findViewById(R.id.ivWeatherCondition);

        int weatherImageId = OpenWeatherUtils.geResourceIdForWeatherCondition(weatherEntity.getWeather_id());


        tvCityName.setText(weatherEntity.getName() + " , " + weatherEntity.getSys_country());
        tvCurrentTemp.setText(OpenWeatherUtils.formatTemperature(getActivity(), weatherEntity.getMain_temp()));
        tvWeatherMain.setText(weatherEntity.getWeather_main());
        tvWeatherDescription.setText(weatherEntity.getWeather_description());
        tvMinMaxTemp.setText(OpenWeatherUtils.formatTemperature(getActivity(), weatherEntity.getMain_temp_max()) + " / " +
                OpenWeatherUtils.formatTemperature(getActivity(), weatherEntity.getMain_temp_min()));

        tvWindSpeed.setText(OpenWeatherUtils.getFormattedWind(getActivity(), weatherEntity.getWind_speed(), weatherEntity.getWind_deg()));

        tvHumidity.setText(weatherEntity.getMain_humidity() + "%");
        tvSunrise.setText(OpenWeatherDateUtils.getHourFromLongTimeSeconds(weatherEntity.getSys_sunrise()));
        tvSunset.setText(OpenWeatherDateUtils.getHourFromLongTimeSeconds(weatherEntity.getSys_sunset()));

        tvLastUpdate.setText(OpenWeatherDateUtils.getFriendlyDateString(weatherEntity.getDt())); //Update last update value


        ivWeatherCondition.setImageResource(weatherImageId);

    }
}
