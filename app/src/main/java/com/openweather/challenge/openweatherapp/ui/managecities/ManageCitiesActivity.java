/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

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
