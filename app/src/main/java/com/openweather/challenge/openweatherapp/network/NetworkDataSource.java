package com.openweather.challenge.openweatherapp.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
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
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.model.WeatherResponse;
import com.openweather.challenge.openweatherapp.model.WeathersResponse;
import com.openweather.challenge.openweatherapp.network.services.OpenWeatherAppFirebaseJobService;
import com.openweather.challenge.openweatherapp.network.services.OpenWeatherAppSyncIntentService;
import com.openweather.challenge.openweatherapp.model.Resource;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by manunez on 27/11/2015.
 */
public class NetworkDataSource {
    private static final String TAG = NetworkDataSource.class.getSimpleName();

    private static final int TIMEOUT_MS = 60000; //60 segundos

    // Interval at which to sync with the weather. Use TimeUnit for convenience, rather than
    // Acording to https://openweathermap.org/price, free account allows Weather API data update < 2 hours. So, we decide an interval every 3 hours
    private static final int SYNC_INTERVAL_HOURS = 1;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static final String WEATHER_SYNC_TAG = "weather-sync";

    // For Singleton instantiation
    private static NetworkDataSource INSTANCE;

    // LiveData storing the latest downloaded weather forecasts
    private final MutableLiveData<Resource<WeatherEntity[]>> responseFromGetCurrentWeathersRequest;
    private final MutableLiveData<Resource<WeatherEntity>> responseFromCurrentWeatherByCityNameRequest;
    private final MutableLiveData<Resource<WeatherEntity>> responseFromCurrentWeatherByCityCoordRequest;
    private final Context context;

    // Volley requestQueue
    private RequestQueue mRequestQueue;


    private NetworkDataSource(Context context) {
        this.context = context.getApplicationContext();
        mRequestQueue = getRequestQueue();

        responseFromGetCurrentWeathersRequest = new MutableLiveData<>();
        responseFromCurrentWeatherByCityNameRequest = new MutableLiveData<>();
        responseFromCurrentWeatherByCityCoordRequest = new MutableLiveData<>();
    }


    public static synchronized NetworkDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (NetworkDataSource.class) {
                INSTANCE = new NetworkDataSource(context);
                Log.d(TAG, "Made new NetworkDataSource");

            }
        }
        return INSTANCE;
    }

    //***********************************
     /*    SERVICES related operations
     /*************************************/

    /**
     * Starts an intent service to fetch the weather.
     */
    public void startFetchWeatherService() {
        Intent intentToFetch = new Intent(context, OpenWeatherAppSyncIntentService.class);
        context.startService(intentToFetch);
        Log.d(TAG, "Service created");
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
        Log.d(TAG, "Job scheduled");
    }

    /**
     * Determines if new data should it fetched from the server. Acording to the Sync interval,
     * controls if the current time minus the last update time register in the DB, are correct.
     *
     * @param now
     * @param lastUpdate
     * @return
     */
    public boolean isSyncNeeded(long now, long lastUpdate) {
        return ((now - lastUpdate) >= (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS));
    }


    //**************************************
     /*        Volley Configuration
     //************************************/

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
            Log.d(TAG, "Cancel pending request for: " + tag.toString());
            mRequestQueue.cancelAll(tag);
        }
    }

    private void getRequestString(final String webserviceUrl,
                                  Response.Listener<String> listener, Response.ErrorListener errorListener) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, webserviceUrl, listener, errorListener) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
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

    //********************
    //  Network requests
    //********************

    /**
     * Get the current weather of city by name. Used in {@link com.openweather.challenge.openweatherapp.ui.addcity.AddCityFragment}
     * to implement the search.
     *
     * @param {@link String} location with the name of the city.
     */
    public void fetchCurrentWeatherByCityName(String location) {
        URL url = NetworkUtils.getCurrentWeatherURLByCityName(location);
        getRequestString(url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "requestCurrentWeathersByCityIDs Response: " + response);
                WeatherResponse weatherResponse = new Gson().fromJson(response, WeatherResponse.class);


                //*******POSTING VALUE TO LIVEDATA
                Resource<WeatherEntity> resourceResponse = null;

                if (weatherResponse.cod == 200) //Success cod 200
                    resourceResponse = Resource.success(weatherResponse.getWeatherEntity());
                else
                    resourceResponse = Resource.error(weatherResponse.message, null);

                responseFromCurrentWeatherByCityNameRequest.postValue(resourceResponse); //post the value

                Log.d(TAG, "requestCurrentWeathersByCityIDs Response JSON: " + weatherResponse.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO do something error
                responseFromCurrentWeatherByCityNameRequest.postValue(Resource.error(error.getMessage(), null));

                Log.d(TAG, "requestCurrentWeathersByCityIDs Error response: " + error.getMessage());

            }
        });

    }


    /**
     * Fetch the current weather for a list of citiesID. Use by {@link OpenWeatherAppFirebaseJobService} and {@link OpenWeatherAppSyncIntentService}
     * to schedule a job to fetch updated entries every Sync time established.
     *
     * @param citiesID list of citiesID from the database that requires update
     */
    public void fetchCurrentWeathersByCityIDs(String citiesID) {
        URL url = NetworkUtils.getCurrentWeatherURLByListCityId(citiesID);
        getRequestString(url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "requestCurrentWeathersByCityIDs Response: " + response);
                WeathersResponse weathersResponse = new Gson().fromJson(response, WeathersResponse.class);
                WeatherEntity[] weatherEntities = new WeatherEntity[weathersResponse.list.length];


                //Convert the array of weatherresponse to array of weather entity, to usable it for DB operations later
                for (int i = 0; i < weathersResponse.list.length; i++)
                    weatherEntities[i] = weathersResponse.list[i].getWeatherEntity();

                //*******POSTING VALUE TO LIVEDATA
                Resource<WeatherEntity[]> resourceResponse = Resource.success(weatherEntities);
                responseFromGetCurrentWeathersRequest.postValue(resourceResponse);

                Log.d(TAG, "requestCurrentWeathersByCityIDs Response JSON: " + weathersResponse.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO do something error
                responseFromGetCurrentWeathersRequest.postValue(Resource.error(error.getMessage(), null));

                Log.d(TAG, "requestCurrentWeathersByCityIDs Error response JSON: " + error.getMessage());
            }
        });

    }


    /**
     * @param lat
     * @param lon
     */
    public void fetchCurrentWeatherByCityCoord(String lat, String lon) {
        URL url = NetworkUtils.getCurrentWeatherURLByCityCoord(lat, lon);
        getRequestString(url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "fetchCurrentWeatherByCityCoord Response: " + response);
                WeatherResponse weatherResponse = new Gson().fromJson(response, WeatherResponse.class);


                //*******POSTING VALUE TO LIVEDATA
                Resource<WeatherEntity> resourceResponse = Resource.success(weatherResponse.getWeatherEntity());
                responseFromCurrentWeatherByCityCoordRequest.postValue(resourceResponse);

                Log.d(TAG, "fetchCurrentWeatherByCityCoord Response JSON: " + weatherResponse.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO do something error
                responseFromCurrentWeatherByCityCoordRequest.postValue(Resource.error(error.getMessage(), null));
                Log.d(TAG, "Response JSON: " + error.getMessage());


            }
        });

    }

    //
//    /**
//     * Get the current weather of city by ID
//     *
//     * @return
//     */
//    public void fetchCurrentWeatherByCityID(String cityID) {
//        URL url = NetworkUtils.getCurrentWeatherURLByCityId(cityID);
//        getRequestString(url.toString(), new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG,"fetchCurrentWeatherByCityID Response: " + response);
//                WeatherResponse weatherResponse = new Gson().fromJson(response, WeatherResponse.class);
////                mDownloadedWeatherForecast.postValue(weatherResponse.getWeatherEntity());
//                Log.d(TAG,"fetchCurrentWeatherByCityID Response JSON: " + weatherResponse.toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
////                String errorMessage = VolleyErrorHelper.getMessage(error, context);
////                OpenWeatherApp.Logger.e(errorMessage);
////                if (errorMessage != null && !errorMessage.isEmpty()) {
////                    Toast.makeText(context.getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
////                }
//            }
//        });
//    }

    //    public void fetchIconImage(String imageId) {
//        URL url = NetworkUtils.getImageURL(imageId);
//
//        requestImage(url.toString(), new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap response) {
//
//            }
//        }, 300, 200, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//    }

    /**
     * Get the current weather of a list of cities ID.
     *
     * @return {@link LiveData} representing the response of the request requestCurrentWeathersByCityIDs()
     */
    public LiveData<Resource<WeatherEntity[]>> responseFromCurrentWeathersByCityIDs() {
        return responseFromGetCurrentWeathersRequest;
    }

    /**
     * Get the current weather of city by name.
     *
     * @return {@link LiveData} representing the response of the request requestCurrentWeatherByCityName()
     */
    public LiveData<Resource<WeatherEntity>> responseFromCurrentWeatherByCityName() {
        return responseFromCurrentWeatherByCityNameRequest;
    }

    /**
     * Get the current weather of city by its coordinates.
     *
     * @return {@link LiveData} representing the response of the request fetchCurrentWeatherByCityCoord()
     */
    public LiveData<Resource<WeatherEntity>> responseFromCurrentWeatherByCityCoord() {
        return responseFromCurrentWeatherByCityCoordRequest;
    }


}
