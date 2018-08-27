package com.openweather.challenge.openweatherapp.utils;

import android.content.Context;

import com.openweather.challenge.openweatherapp.repository.AppRepository;
import com.openweather.challenge.openweatherapp.db.AppRoomDatabase;
import com.openweather.challenge.openweatherapp.network.NetworkDataSource;
import com.openweather.challenge.openweatherapp.ui.addcity.AddCityViewModelFactory;
import com.openweather.challenge.openweatherapp.ui.managecities.ManageCitiesViewModelFactory;
import com.openweather.challenge.openweatherapp.ui.showweather.ShowWeatherViewModelFactory;

/**
 * Provides static methods to inject the various classes needed for this weather application
 */
public class InjectorUtils {

    public static AppRepository provideRepository(Context context) {
        AppRoomDatabase database = AppRoomDatabase.getInstance(context.getApplicationContext());
        NetworkDataSource networkDataSource = NetworkDataSource.getInstance(context.getApplicationContext());
        return AppRepository.getInstance(database.weatherDao(), networkDataSource);
    }

    public static NetworkDataSource provideNetworkDataSource(Context context) {
        // This call to provide repository is necessary if the app starts from a service - in this
        // case the repository will not exist unless it is specifically created.
        provideRepository(context.getApplicationContext());
        return NetworkDataSource.getInstance(context.getApplicationContext());
    }

    public static AddCityViewModelFactory provideAddCityViewModelFactory(Context context) {
        AppRepository repository = provideRepository(context.getApplicationContext());
        return new AddCityViewModelFactory(repository);
    }

    public static ShowWeatherViewModelFactory provideShowWeatherViewModelFactory(Context context) {
        AppRepository repository = provideRepository(context.getApplicationContext());
        return new ShowWeatherViewModelFactory(repository);
    }

    public static ManageCitiesViewModelFactory provideManageCitiesViewModelFactory(Context context) {
        AppRepository repository = provideRepository(context.getApplicationContext());
        return new ManageCitiesViewModelFactory(repository);
    }


}