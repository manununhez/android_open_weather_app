package com.openweather.challenge.openweatherapp.db.entity;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.openweather.challenge.openweatherapp.network.NetworkUtils;

/**
 * Created by manuel on 29,August,2018
 */
public final class BindingAdapters {
    private static final String TAG = BindingAdapters.class.getSimpleName();
    private BindingAdapters() {
        //NO-OP
    }

    @BindingAdapter("weather_icon")
    public static void loadImage(ImageView imageView, String urlIcon) {
        String url = NetworkUtils.getCurrentWeatherURLIconImage(urlIcon).toString();
        Log.d(TAG, url);
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }
}
