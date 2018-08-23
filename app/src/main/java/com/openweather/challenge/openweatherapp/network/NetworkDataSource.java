package com.openweather.challenge.openweatherapp.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.openweather.challenge.openweatherapp.OpenWeatherApp;
import com.openweather.challenge.openweatherapp.model.WeatherResponse;

import java.net.URL;

/**
 * Created by manunez on 27/11/2015.
 */
public class NetworkDataSource {
    private static final String TAG = NetworkDataSource.class
            .getSimpleName();
    private static final int TIMEOUT_MS = 60000; //60 segundos
    private static NetworkDataSource INSTANCE;
    private RequestQueue mRequestQueue;
    private Context context;

    private NetworkDataSource(Context context) {
        this.context = context.getApplicationContext();
        mRequestQueue = getRequestQueue();

    }


    public static synchronized NetworkDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (NetworkDataSource.class) {
                INSTANCE = new NetworkDataSource(context);
                OpenWeatherApp.Logger.d("Made new NetworkDataSource");

            }
        }
        return INSTANCE;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    private <T> void addToRequestQueue(Request<T> req, Object tag) {
        // set the default tag if tag is empty
        req.setTag(tag == null ? TAG : tag);
        getRequestQueue().add(req);
        if (req instanceof RequestQueue.RequestFinishedListener)
            getRequestQueue().addRequestFinishedListener((RequestQueue.RequestFinishedListener<Object>) req);
    }

    private <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    private void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            OpenWeatherApp.Logger.d("Se finaliza el request para: " + tag.toString());
            mRequestQueue.cancelAll(tag);
        }
    }

    private void getRequestString(final String webserviceUrl,
                                  final byte[] requestParams, Response.Listener<String> listener, Response.ErrorListener errorListener) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, webserviceUrl, listener, errorListener) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                final byte[] body = requestParams;
                if (body != null) {
                    return body;
                }
                return super.getBody();
            }
        };

        stringRequest.setShouldCache(false);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        // Adding String request to request queue
        INSTANCE.addToRequestQueue(stringRequest, webserviceUrl);
    }


    //Image Request
    private void requestImage(final String webserviceUrl, Response.Listener<Bitmap> bitmapListener,
                              int maxWidth, int maxHeight, ImageView.ScaleType scaleType,
                              final Bitmap.Config decodeConfig, Response.ErrorListener errorListener) {

        ImageRequest imageRequest = new ImageRequest(webserviceUrl,
                bitmapListener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);

        // Adding String request to request queue
        INSTANCE.addToRequestQueue(imageRequest, webserviceUrl);

    }


    public void getIconImage(String imageId) {
        URL url = NetworkUtils.getImageURL(imageId);

        requestImage(url.toString(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

            }
        }, 300, 200, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public void getCurrentWeather(String location) {
        URL url = NetworkUtils.getCurrentWeatherURL(location);
        getRequestString(url.toString(), null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                OpenWeatherApp.Logger.d("Response: " + response);
                WeatherResponse weatherResponse = new Gson().fromJson(response, WeatherResponse.class);
                OpenWeatherApp.Logger.d("Response JSON: " + weatherResponse.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = VolleyErrorHelper.getMessage(error, context);
                OpenWeatherApp.Logger.e(errorMessage);
                if (errorMessage != null && !errorMessage.isEmpty()) {
                    Toast.makeText(context.getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
