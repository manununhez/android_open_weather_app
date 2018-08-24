package com.openweather.challenge.openweatherapp.ui.showweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.openweather.challenge.openweatherapp.R;

import java.util.Objects;

public class ShowWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_weather_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ShowWeatherFragment.newInstance())
                    .commitNow();
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle("OpenWeatherMap");  // provide compatibility to all the versions
    }
}
