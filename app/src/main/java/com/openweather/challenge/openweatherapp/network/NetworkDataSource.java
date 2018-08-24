package com.openweather.challenge.openweatherapp.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.google.gson.Gson;
import com.openweather.challenge.openweatherapp.OpenWeatherApp;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.model.WeatherResponse;
import com.openweather.challenge.openweatherapp.model.WeathersResponse;
import com.openweather.challenge.openweatherapp.network.services.OpenWeatherAppFirebaseJobService;
import com.openweather.challenge.openweatherapp.network.services.OpenWeatherAppSyncIntentService;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by manunez on 27/11/2015.
 */
public class NetworkDataSource {
    private static final String TAG = NetworkDataSource.class
            .getSimpleName();
    private static final int TIMEOUT_MS = 60000; //60 segundos

    // Interval at which to sync with the weather. Use TimeUnit for convenience, rather than
    // Acording to https://openweathermap.org/price, free account allows Weather API data update < 2 hours. So, we decide an interval every 3 hours
    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static final String WEATHER_SYNC_TAG = "weather-sync";

    // For Singleton instantiation
    private static NetworkDataSource INSTANCE;

    // LiveData storing the latest downloaded weather forecasts
    private final MutableLiveData<WeatherEntity[]> responseFromGetCurrentWeathers;
    private final MutableLiveData<WeatherEntity> responseWeatherByCityName;
    private final Context context;

    // Volley requestQueue
    private RequestQueue mRequestQueue;


    private NetworkDataSource(Context context) {
        this.context = context.getApplicationContext();
        mRequestQueue = getRequestQueue();

        responseFromGetCurrentWeathers = new MutableLiveData<>();
        responseWeatherByCityName = new MutableLiveData<>();
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

    /**************************************
     *              SERVICES
     ******************************************/


    /**
     * Starts an intent service to fetch the weather.
     */
    public void startFetchWeatherService() {
        Intent intentToFetch = new Intent(context, OpenWeatherAppSyncIntentService.class);
        context.startService(intentToFetch);
        OpenWeatherApp.Logger.d("Service created");
    }


    /**
     * Schedules a repeating job service which fetches the weather.
     */
    public void scheduleRecurringFetchWeatherSync() {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        // Create the Job to periodically sync Sunshine
        Job syncSunshineJob = dispatcher.newJobBuilder()
                /* The Service that will be used to sync Sunshine's data */
                .setService(OpenWeatherAppFirebaseJobService.class)
                /* Set the UNIQUE tag used to identify this Job */
                .setTag(WEATHER_SYNC_TAG)
                /*
                 * Network constraints on which this Job should run. We choose to run on any
                 * network, but you can also choose to run only on un-metered networks or when the
                 * device is charging. It might be a good idea to include a preference for this,
                 * as some users may not want to download any data on their mobile plan. ($$$)
                 */
                .setConstraints(Constraint.ON_ANY_NETWORK)
                /*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.
                 */
                .setLifetime(Lifetime.FOREVER)
                /*
                 * We want Sunshine's weather data to stay up to date, so we tell this Job to recur.
                 */
                .setRecurring(true)
                /*
                 * We want the weather data to be synced every 3 to 4 hours. The first argument for
                 * Trigger's static executionWindow method is the start of the time frame when the
                 * sync should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                /*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 */
                .setReplaceCurrent(true)
                /* Once the Job is ready, call the builder's build method to return the Job */
                .build();

        // Schedule the Job with the dispatcher
        dispatcher.schedule(syncSunshineJob);
        OpenWeatherApp.Logger.d("Job scheduled");
    }

    /**
     * @param today
     * @param lastUpdate
     * @return
     */
    public boolean isSyncNeeded(long today, long lastUpdate) {
        return ((lastUpdate - today) >= (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS));
    }


    /**************************************
     *              Volley Configuration
     ******************************************/

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

    /**************************************
     *              Network requests
     ******************************************/


    /**
     * Get the current weather of city by name
     *
     * @return
     */
    public void fetchCurrentWeatherByCityName(String location) {
        URL url = NetworkUtils.getCurrentWeatherURLByCityName(location);
        getRequestString(url.toString(), null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //TODO add control to "not found city" according to "cod" = 400. Cod = 200, success. Cod = 400, Not found
                OpenWeatherApp.Logger.d("fetchCurrentWeatherByCityName Response: " + response);
                WeatherResponse weatherResponse = new Gson().fromJson(response, WeatherResponse.class);
                responseWeatherByCityName.postValue(weatherResponse.getWeatherEntity());
                OpenWeatherApp.Logger.d("fetchCurrentWeatherByCityName Response JSON: " + weatherResponse.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                String errorMessage = VolleyErrorHelper.getMessage(error, context);
//                OpenWeatherApp.Logger.e(errorMessage);
//                if (errorMessage != null && !errorMessage.isEmpty()) {
//                    OpenWeatherApp.Logger.d("Response: " + errorMessage);
//                }
                OpenWeatherApp.Logger.d("fetchCurrentWeatherByCityName Response JSON: " + error.getMessage());

            }
        });

    }


    /**
     * Get the current weather of all the cities stored
     *
     * @return
     */
    public void fetchCurrentWeathersByCitiesID(String citiesID) {
        URL url = NetworkUtils.getCurrentWeatherURLByListCityId(citiesID);
        getRequestString(url.toString(), null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                OpenWeatherApp.Logger.d("fetchCurrentWeathersByCitiesID Response: " + response);
                WeathersResponse weathersResponse = new Gson().fromJson(response, WeathersResponse.class);
                WeatherEntity[] weatherEntities = new WeatherEntity[weathersResponse.list.length];

                //Convert the array of weatherresponse to array of weather entity, to make it use to use with the DB later
                for (int i = 0; i < weathersResponse.list.length; i++)
                    weatherEntities[i] = weathersResponse.list[i].getWeatherEntity();

                responseFromGetCurrentWeathers.postValue(weatherEntities);

                OpenWeatherApp.Logger.d("fetchCurrentWeathersByCitiesID Response JSON: " + weathersResponse.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                String errorMessage = VolleyErrorHelper.getMessage(error, context);
//                OpenWeatherApp.Logger.e(errorMessage);
//                if (errorMessage != null && !errorMessage.isEmpty()) {
                OpenWeatherApp.Logger.d("Response JSON: " + error.getMessage());
//                    Toast.makeText(context.getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
//                }
            }
        });

    }


    /**
     * Get the current weather of city by ID
     *
     * @return
     */
    public void fetchCurrentWeatherByCityID(String cityID) {
        URL url = NetworkUtils.getCurrentWeatherURLByCityId(cityID);
        getRequestString(url.toString(), null, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                OpenWeatherApp.Logger.d("fetchCurrentWeatherByCityID Response: " + response);
                WeatherResponse weatherResponse = new Gson().fromJson(response, WeatherResponse.class);
//                mDownloadedWeatherForecast.postValue(weatherResponse.getWeatherEntity());
                OpenWeatherApp.Logger.d("fetchCurrentWeatherByCityID Response JSON: " + weatherResponse.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                String errorMessage = VolleyErrorHelper.getMessage(error, context);
//                OpenWeatherApp.Logger.e(errorMessage);
//                if (errorMessage != null && !errorMessage.isEmpty()) {
//                    Toast.makeText(context.getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
//                }
            }
        });

    }

    /**
     * Get the current weather of all the cities stored
     *
     * @return
     */
    public LiveData<WeatherEntity[]> getCurrentWeathers() {
        return responseFromGetCurrentWeathers;
    }

    /**
     * Get the current weather of city by name
     *
     * @return
     */
    public LiveData<WeatherEntity> getCurrentWeatherByCityName() {
        return responseWeatherByCityName;
    }


}
