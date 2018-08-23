package com.openweather.challenge.openweatherapp.network;

import android.content.Context;
import android.net.Uri;

import com.openweather.challenge.openweatherapp.OpenWeatherApp;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ramya on 11/8/17.
 */

public class NetworkUtils {
    public static final String OPEN_WEATHER_API = "http://api.openweathermap.org/data/2.5/weather";
    public static final String OPEN_WEATHER_IMAGE_API = "http://openweathermap.org/img/w/";
    public static final String WEATHER_FORECAST_PARAM = "q";
    public static final String UNITS_PARAM = "units";
    public static final String METRICS_PARAM = "metric"; //Temperature unit in CELSIUS
    public static final String APP_ID_PARAM = "APPID";
    public static final String APP_ID = "0d43146b57d6b8c35d753502656efe4c";
    public static final String PNG_EXTENSION = ".png";
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private Context context;


    private NetworkUtils() {
    }

    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param locationQuery The location that will be queried for.
     * @return The URL to use to query the weather server.
     */
    private static URL buildUrlWithLocationQuery(String locationQuery) {
        Uri weatherQueryUri = Uri.parse(OPEN_WEATHER_API).buildUpon()
                .appendQueryParameter(WEATHER_FORECAST_PARAM, locationQuery)
                .appendQueryParameter(UNITS_PARAM, METRICS_PARAM)
                .appendQueryParameter(APP_ID_PARAM, APP_ID)
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            OpenWeatherApp.Logger.d("URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static URL buildUrlGetImage(String imageId) {
        Uri uri  = Uri.parse(OPEN_WEATHER_IMAGE_API).buildUpon()
                .appendPath(imageId + PNG_EXTENSION)
                .build();

        try {
            URL weatherQueryUrl = new URL(uri.toString());
            OpenWeatherApp.Logger.d("URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getCurrentWeatherURL(String locationQuery){
        return buildUrlWithLocationQuery(locationQuery);
    }

    public static URL getImageURL(String imageId){
        return buildUrlGetImage(imageId);
    }
}
