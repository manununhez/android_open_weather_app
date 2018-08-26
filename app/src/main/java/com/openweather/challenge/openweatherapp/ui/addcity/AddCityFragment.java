package com.openweather.challenge.openweatherapp.ui.addcity;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.openweather.challenge.openweatherapp.OpenWeatherApp;
import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.utils.CurrentLocationListener;
import com.openweather.challenge.openweatherapp.utils.InjectorUtils;
import com.openweather.challenge.openweatherapp.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;

public class AddCityFragment extends Fragment implements SearchView.OnQueryTextListener, AddCitySearchAdapter.OnItemClickListener, View.OnClickListener {

    private static final String QUERY = "query_search";
    private static final String WEATHER_ENTITIES = "weather_entities_search";
    private static final boolean PRESSED = true;
    private static final boolean NOT_PRESSED = false;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1000;
    private static final int REQUEST_CODE_SOURCE_SETTINGS = 2000;
    private final String TAG = AddCityFragment.class.getSimpleName();
    private AddCityViewModel mViewModel;
    private TextView tvCountResults;
    private View rootView;
    private AddCitySearchAdapter mAdapter;
    private ArrayList<WeatherEntity> weatherEntities;
    private ArrayList<String> queriesHistory;
    private boolean ifBtnLocationHasBeenPressed = NOT_PRESSED;

    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_city_fragment, container, false);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putStringArrayList(QUERY, queriesHistory);
        savedInstanceState.putParcelableArrayList(WEATHER_ENTITIES, weatherEntities);

        // Always call the superclass so it can save the rootView hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null &&
                savedInstanceState.getStringArrayList(QUERY) != null &&
                savedInstanceState.getParcelableArrayList(WEATHER_ENTITIES) != null) { //In getActivity() way, after onDestroyView() might happen and restart, we will sure to init with variables not null
            // Restore value of members from saved state
            queriesHistory = savedInstanceState.getStringArrayList(QUERY);
            weatherEntities = savedInstanceState.getParcelableArrayList(WEATHER_ENTITIES);
        } else {
            // Probably initialize members with default values for a new instance
            queriesHistory = new ArrayList<>();
            weatherEntities = new ArrayList<>();

        }


        // Get the ViewModel from the factory
        AddCityViewModelFactory factory = InjectorUtils.provideAddCityViewModelFactory(Objects.requireNonNull(getActivity()).getApplicationContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(AddCityViewModel.class);

        tvCountResults = rootView.findViewById(R.id.tvCountResults);
        Button btnCurrenLocation = rootView.findViewById(R.id.btnCurrentLocation);
        btnCurrenLocation.setOnClickListener(this);
        SearchView searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);


        initRecyclerView();


    }


    private void initRecyclerView() {
        RecyclerView mRecyclerView = rootView.findViewById(R.id.rvSearch);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new AddCitySearchAdapter(weatherEntities, this);
        mRecyclerView.setAdapter(mAdapter);

        LiveData<WeatherEntity> weatherSearchByNameResponse = mViewModel.responseFromCurrentWeatherByCityName();
        weatherSearchByNameResponse.observe(this, response -> {
            OpenWeatherApp.Logger.d("AddCityFragment = " + Objects.requireNonNull(response).toString());
            //TODO getActivity() can be improved
            if (queriesHistory != null && !queriesHistory.isEmpty()) { // If at least one query was made, getActivity() response is correct. getActivity() avoid to have elements in the recyclerView from previous searches.
                if (!findItemInTheList(weatherEntities, response)) { //We only add a new element if it does not exist in the list yet.
                    weatherEntities.add(response);

                    mAdapter.notifyDataSetChanged();
                    if (mAdapter != null)
                        tvCountResults.setText(getString(R.string.found_results, mAdapter.getItemCount()));

                }
            }
        });


        LiveData<WeatherEntity> weatherSearchByCityCoordResponse = mViewModel.responseFromCurrentWeatherByCityCoord();
//weatherSearchByCityCoordResponse.re
        weatherSearchByCityCoordResponse.observe(this, response -> {
            if (ifBtnLocationHasBeenPressed) { //we read the values of this observer only if the user has selected to get current location
                OpenWeatherApp.Logger.d("AddCityFragment = " + Objects.requireNonNull(response).toString());
                OpenWeatherApp.Logger.d("New values inserted");

                mViewModel.insertWeather(response);
                Objects.requireNonNull(getActivity()).finish();
//                if (!findItemInTheList(weatherEntities, response)) { //We only add a new element if it does not exist in the list yet.
//                    weatherEntities.add(response);
//
//                    mAdapter.notifyDataSetChanged();
//                    if (mAdapter != null)
//                        tvCountResults.setText(mAdapter.getItemCount() + " results");
//
//                }
            }
        });


    }


    private boolean findItemInTheList(ArrayList<WeatherEntity> weatherEntities, WeatherEntity weatherEntity) {
        for (WeatherEntity w : weatherEntities) {
            if (w.getId() == weatherEntity.getId() && w.getDt() == weatherEntity.getDt()) //Id and datetime
                return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (!s.isEmpty()) {
            String allRemoved = s.replaceAll("^\\s+|\\s+$", ""); //trim white spaces, front and back of the text
            if (!queriesHistory.contains(allRemoved)) { //Avoid repeat the same queries to prevent inaccuracies calls
                mViewModel.fetchCurrentWeatherByCityName(allRemoved);
                queriesHistory.add(allRemoved);
            }

        } else {
            weatherEntities.clear();
            mAdapter.notifyDataSetChanged();
            if (mAdapter != null) tvCountResults.setText(getString(R.string.found_results, mAdapter.getItemCount()));

        }
        return true;
    }

    //FilterClick
    @Override
    public void onItemClick(WeatherEntity item) {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            // Insert our new weather data into OpenWeatherApp's database
            mViewModel.insertWeather(item);
            OpenWeatherApp.Logger.d("New values inserted");

            Objects.requireNonNull(getActivity()).finish();

        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCurrentLocation:
                startReceivingCoordinates();
                break;
            default:
        }
    }


    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            checkLocationServices();
        }
    }

    private void getLocationUpdates() {
        CurrentLocationListener.getInstance(getActivity()).observe(this, new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {
                if (location != null) {
                    Log.d(TAG,
                            "Location Changed " + location.getLatitude() + " : " + location.getLongitude());
//                    Toast.makeText(getActivity(), location.getLatitude() + " : " + location.getLongitude(), Toast.LENGTH_SHORT).show();

                    mViewModel.fetchCurrentWeatherByCityCoords(String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude()));


                    CurrentLocationListener.getInstance(getActivity()).removeObserver(this);//We receive the coords only once and then we cancel the observer to stop receiving location
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startReceivingCoordinates();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //if (resultCode == Activity.RESULT_OK)
            if (requestCode == REQUEST_CODE_SOURCE_SETTINGS)
                startReceivingCoordinates();

    }

    private void startReceivingCoordinates() {
        ifBtnLocationHasBeenPressed = PRESSED;
        checkLocationPermission();
    }

    private void checkLocationServices() {

        LocationManager lm = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = Objects.requireNonNull(lm).isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }

        try {
            network_enabled = Objects.requireNonNull(lm).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            Utils.getAlertDialogWithChoice(getActivity(), getResources().getString(R.string.title_alert_dialog), getResources().getString(R.string.gps_network_not_enabled),
                    getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(myIntent, REQUEST_CODE_SOURCE_SETTINGS);
                        }
                    }, getActivity().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {

                        }
                    }).show();

        } else {
            getLocationUpdates();
        }
    }
}
