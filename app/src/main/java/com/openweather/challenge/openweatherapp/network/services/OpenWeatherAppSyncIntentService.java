/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.network.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.openweather.challenge.openweatherapp.repository.AppRepository;
import com.openweather.challenge.openweatherapp.utils.InjectorUtils;


/**
 * An {@link IntentService} subclass for immediately scheduling a sync with the server off of the
 * main thread. This is necessary because {@link com.firebase.jobdispatcher.FirebaseJobDispatcher}
 * will not trigger a job immediately. This should only be called when the application is on the
 * screen.
 */
public class OpenWeatherAppSyncIntentService extends IntentService {
    private static final String LOG_TAG = OpenWeatherAppSyncIntentService.class.getSimpleName();

    public OpenWeatherAppSyncIntentService() {
        super("OpenWeatherAppSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "Intent service started");

        AppRepository appRepository = InjectorUtils.provideRepository(this.getApplicationContext());
        appRepository.fetchCurrentWeathersByCityIDs();

    }
}