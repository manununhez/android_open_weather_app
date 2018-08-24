package com.openweather.challenge.openweatherapp.ui.addcity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.openweather.challenge.openweatherapp.R;

import java.util.Objects;

public class AddCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AddCityFragment.newInstance())
                    .commitNow();
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle("Add New City");  // provide compatibility to all the versions

    }



}
