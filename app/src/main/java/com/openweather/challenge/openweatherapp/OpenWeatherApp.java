package com.openweather.challenge.openweatherapp;

import android.app.Application;
import android.util.Log;

/**
 * Created by manuel on 21,August,2018
 */
public class OpenWeatherApp extends Application {



    private static final String TAG = "com.opweather.challenge";

    public static class Logger {
        public static void i(String message) {
            if (message == null) {
                message = "Sin mensaje";
            }
            Log.i(TAG, message);
        }

        public static void w(String message) {
            if (message == null) {
                message = "Sin mensaje";
            }
            Log.w(TAG, message);
        }

        public static void e(String message) {
            if (message == null) {
                message = "Sin mensaje";
            }
            Log.e(TAG, message);
        }

        public static void d(String message) {
            if (message == null) {
                message = "Sin mensaje";
            }
            Log.d(TAG, message);
        }

    }

}
