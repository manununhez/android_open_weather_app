package com.openweather.challenge.openweatherapp.ui.managecities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.ui.managecities.ManageCitiesFragment;

import java.util.Objects;

public class ManageCitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_cities_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ManageCitiesFragment.newInstance())
                    .commitNow();
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle("My Cities");  // provide compatibility to all the versions

    }
}
