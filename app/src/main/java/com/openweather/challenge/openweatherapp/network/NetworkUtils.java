package com.openweather.challenge.openweatherapp.network;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;


public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String OPEN_WEATHER_API = "weather";
    private static final String OPEN_WEATHER_GROUP_API = "group";
    private static final String OPEN_WEATHER_IMAGE_API = "http://openweathermap.org/img/w/";
    private static final String WEATHER_FORECAST_PARAM = "q";
    private static final String CITY_ID = "id";
    private static final String CITY_COORD_LAT = "lat";
    private static final String CITY_COORD_LON = "lon";
    private static final String UNITS_PARAM = "units";
    private static final String METRICS_PARAM = "metric"; //Temperature unit in CELSIUS
    private static final String APP_ID_PARAM = "APPID";
    private static final String API_KEY = "0d43146b57d6b8c35d753502656efe4c";
    private static final String PNG_EXTENSION = ".png";


    private NetworkUtils() {
    }

    private static URL builImageUrl(String iconUrl) {
        Uri weatherQueryUri = Uri.parse(OPEN_WEATHER_IMAGE_API.concat(iconUrl).concat(PNG_EXTENSION)).buildUpon()
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.d(TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Builds the URL used to talk to the weather server using a location.
     *
     * @param locationQuery The location that will be queried for.
     * @return The URL to use to query the weather server.
     */
    private static URL buildUrlByCityName(String locationQuery) {
        Uri weatherQueryUri = Uri.parse(BASE_URL.concat(OPEN_WEATHER_API)).buildUpon()
                .appendQueryParameter(WEATHER_FORECAST_PARAM, locationQuery)
                .appendQueryParameter(UNITS_PARAM, METRICS_PARAM)
                .appendQueryParameter(APP_ID_PARAM, API_KEY)
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.d(TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Builds the URL used to talk to the weather server using an ID.
     *
     * @param cityID The City ID that will be queried for.
     * @return The URL to use to query the weather server.
     */
    private static URL buildUrlByCityId(String cityID) {
        Uri weatherQueryUri = Uri.parse(BASE_URL.concat(OPEN_WEATHER_API)).buildUpon()
                .appendQueryParameter(CITY_ID, cityID)
                .appendQueryParameter(UNITS_PARAM, METRICS_PARAM)
                .appendQueryParameter(APP_ID_PARAM, API_KEY)
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.d(TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Builds the URL used to talk to the weather server using coordinates lat and lon.
     *
     * @param lat Latitude
     * @param lon Longitude
     * @return The URL to use to query the weather server.
     */
    private static URL buildUrlByCityCoordinates(String lat, String lon) {
        Uri weatherQueryUri = Uri.parse(BASE_URL.concat(OPEN_WEATHER_API)).buildUpon()
                .appendQueryParameter(CITY_COORD_LAT, lat)
                .appendQueryParameter(CITY_COORD_LON, lon)
                .appendQueryParameter(UNITS_PARAM, METRICS_PARAM)
                .appendQueryParameter(APP_ID_PARAM, API_KEY)
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.d(TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Builds the URL used to talk to the weather server using a list of cities ID separated by comma.
     *
     * @param listCityID The list of cities ID that will be queried for.
     * @return The URL to use to query the weather server.
     */
    private static URL buildUrlByCityIdsList(String listCityID) {
        Uri weatherQueryUri = Uri.parse(BASE_URL.concat(OPEN_WEATHER_GROUP_API)).buildUpon()
                .appendQueryParameter(CITY_ID, listCityID)
                .appendQueryParameter(UNITS_PARAM, METRICS_PARAM)
                .appendQueryParameter(APP_ID_PARAM, API_KEY)
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString().replace("%2C", ",")); //Error encoding with list between commas. %2C -> comma
            Log.d(TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getCurrentWeatherURLIconImage(String iconUrl) {
        return builImageUrl(iconUrl);
    }

    /**
     * Returns the URL used to talk to the weather server using a location.
     *
     * @param locationQuery The location that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL getCurrentWeatherURLByCityName(String locationQuery) {
        return buildUrlByCityName(locationQuery);
    }

    /**
     * Returns the URL used to talk to the weather server using a list of cities ID separated by comma.
     *
     * @param listCityID The list of cities ID that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL getCurrentWeatherURLByListCityId(String listCityID) {
        return buildUrlByCityIdsList(listCityID);
    }


    /**
     * Returns the URL used to talk to the weather server using coordinates lat and lon.
     *
     * @param lat Latitude
     * @param lon Longitude
     * @return The URL to use to query the weather server.
     */
    public static URL getCurrentWeatherURLByCityCoord(String lat, String lon) {
        return buildUrlByCityCoordinates(lat, lon);
    }


//    private static URL buildUrlGetImage(String imageId) {
//        Uri uri = Uri.parse(OPEN_WEATHER_IMAGE_API).buildUpon()
//                .appendPath(imageId + PNG_EXTENSION)
//                .build();
//
//        try {
//            URL weatherQueryUrl = new URL(uri.toString());
//            Log.d(TAG,"URL: " + weatherQueryUrl);
//            return weatherQueryUrl;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//    /**
//     //     *
//     //     * @param cityID
//     //     * @return
//     //     */
//    public static URL getCurrentWeatherURLByCityId(String cityID) {
//        return buildUrlByCityId(cityID);
//    }

//    public static URL getImageURL(String imageId) {
//        return buildUrlGetImage(imageId);
//    }
}
