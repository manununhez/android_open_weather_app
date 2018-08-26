package com.openweather.challenge.openweatherapp.utils;


import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class CurrentLocationListener extends LiveData<Location> {

    private static CurrentLocationListener instance;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                // Got last known location. In some rare situations this can be null.
                if (location != null)
                    setValue(location);
            }
        }
    };
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;

    private CurrentLocationListener(Context appContext) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                    setValue(location);
            }
        });
        createLocationRequest();
    }

    public static CurrentLocationListener getInstance(Context appContext) {
        if (instance == null) {
            instance = new CurrentLocationListener(appContext);
        }
        return instance;
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onActive() {
        super.onActive();
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (mLocationCallback != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }


}
