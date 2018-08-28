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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.model.Resource;
import com.openweather.challenge.openweatherapp.utils.CurrentFusedLocationListener;
import com.openweather.challenge.openweatherapp.utils.InjectorUtils;
import com.openweather.challenge.openweatherapp.utils.LoadingDialog;
import com.openweather.challenge.openweatherapp.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;

public class AddCityFragment extends Fragment implements SearchView.OnQueryTextListener, SearchWeatherItemClickCallback, View.OnClickListener {

    private static final String TAG = AddCityFragment.class.getSimpleName();
    private static final String QUERY = "query_search";
    private static final String WEATHER_ENTITIES = "weather_entities_search";
    private static final boolean PRESSED = true;
    private static final boolean NOT_PRESSED = false;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1000;
    private static final int REQUEST_CODE_SOURCE_SETTINGS = 2000;
    private boolean ifBtnLocationHasBeenPressed = NOT_PRESSED;
    private AddCityViewModel mViewModel;
    private TextView tvCountResults;
    private View rootView;
    private AddCitySearchAdapter mAdapter;
    private ArrayList<WeatherEntity> weatherEntities;
    private ArrayList<String> queriesHistory;
    private LoadingDialog loadingDialog;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

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
                savedInstanceState.getParcelableArrayList(WEATHER_ENTITIES) != null) { //In getActivity() way, after onDestroyView() might happen and restart,
            // we will sure to init with variables not null
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

        mProgressBar = rootView.findViewById(R.id.progressBar);
        tvCountResults = rootView.findViewById(R.id.tvCountResults);
        Button btnCurrenLocation = rootView.findViewById(R.id.btnCurrentLocation);
        btnCurrenLocation.setOnClickListener(this);
        SearchView searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);


        instantiateLoading();
        initRecyclerView();

        observeResponseFromCurrentWeatherByCityName();

        observeResponseFromCurrentWeatherByCityCoord();

    }

    private void observeResponseFromCurrentWeatherByCityCoord() {

        LiveData<Resource<WeatherEntity>> weatherSearchByCityCoordResponse = mViewModel.responseFromCurrentWeatherByCityCoord();
        weatherSearchByCityCoordResponse.observe(this, response -> {
            if (response != null) {
                if (response.status.equals(Resource.Status.SUCCESS)) {

                    if (ifBtnLocationHasBeenPressed) { //we read the values of this observer only if the user has selected to get current location
                        Log.d(TAG, "AddCityFragment = " + Objects.requireNonNull(response).toString());
                        Log.d(TAG, "New values inserted");

                        mViewModel.insertWeather(response.data);
                        Toast.makeText(getActivity(), "Added " + response.data.getName() + " as a new city.", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(getActivity()).finish();

                    }

                } else if (response.status.equals(Resource.Status.ERROR)) {
                    //TODO do something error
                    if (response.message != null)
                        Log.d(TAG, response.message);
                    else
                        Log.d(TAG, "Status ERROR");
                }
                dismissLoading();
            }
        });

    }

    private void observeResponseFromCurrentWeatherByCityName() {
        LiveData<Resource<WeatherEntity>> weatherSearchByNameResponse = mViewModel.responseFromCurrentWeatherByCityName();
        weatherSearchByNameResponse.observe(this, response -> {
            if (response != null) {
                if (response.status.equals(Resource.Status.SUCCESS)) {
                    Log.d(TAG, "AddCityFragment = " + Objects.requireNonNull(response).toString());
                    if (queriesHistory != null && !queriesHistory.isEmpty()) { // If at least one query was made, getActivity() response is correct. getActivity() avoid to have elements in the recyclerView from previous searches.
                        if (!findItemInTheList(weatherEntities, response.data)) { //We only add a new element if it does not exist in the list yet.
                            weatherEntities.add(response.data);

                            mAdapter.notifyDataSetChanged();
                            if (mAdapter != null)
                                tvCountResults.setText(getString(R.string.found_results, mAdapter.getItemCount()));

                        }


                    }
                } else if (response.status.equals(Resource.Status.ERROR)) {
                    //TODO do something error
                    if (response.message != null)
                        Log.d(TAG, response.message);
                    else
                        Log.d(TAG, "Status ERROR");
                }

                showWeatherDataRecyclerView();
            }
        });
    }


    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.rvSearch);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new AddCitySearchAdapter(weatherEntities, this);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void instantiateLoading() {
        loadingDialog = new LoadingDialog(getActivity(), getString(R.string.title_loading));
        loadingDialog.setCanceledOnTouchOutside(false);
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
                showProgressBar();
                mViewModel.requestCurrentWeatherByCityName(allRemoved);
                queriesHistory.add(allRemoved);
            }

        } else { //the search is empty
            showWeatherDataRecyclerView();
            queriesHistory.clear(); //Clear the list of queries searched
            weatherEntities.clear(); //Clear the result list (recyclerview)
            mAdapter.notifyDataSetChanged(); //update Recycler
            if (mAdapter != null)
                tvCountResults.setText(getString(R.string.found_results, mAdapter.getItemCount()));

        }
        return true;
    }


    /**
     * Search item click
     * Used by {@link AddCitySearchAdapter}
     *
     * @param weatherEntity
     */
    @Override
    public void onClick(WeatherEntity weatherEntity) {
        Toast.makeText(getActivity(), "Added " + weatherEntity.getName() + " as a new city.", Toast.LENGTH_SHORT).show();

        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            // Insert our new weather data into the database
            mViewModel.insertWeather(weatherEntity);
            Log.d(TAG, "New values inserted");

            Objects.requireNonNull(getActivity()).finish();

        });
    }

    @Override
    public boolean onLongClick(WeatherEntity weatherEntity) {
        //Nothing
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCurrentLocation:
                startReceivingCoordinates();
                showLoading();
                break;
            default:
        }
    }


    //*******************
    // Current Location
    //*******************

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
        CurrentFusedLocationListener.getInstance(getActivity()).observe(this, new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {
                if (location != null) {
                    Log.d(TAG, "Location Changed " + location.getLatitude() + " : " + location.getLongitude());

                    mViewModel.requestCurrentWeatherByCityCoords(String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude()));


                    CurrentFusedLocationListener.getInstance(getActivity()).removeObserver(this);//We receive the coords only once and then we cancel the observer to stop receiving location
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
            dismissLoading();
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


    //********************************************
    // Loading and progress bar related functions
    //********************************************

    private void dismissLoading() {
        loadingDialog.dismiss();
    }


    private void showLoading() {
        loadingDialog.show();
    }

    /**
     * This method will make the loading indicator visible and hide the weather View and error
     * message.
     */
    private void showProgressBar() {
        // Then, hide the weather data
        mRecyclerView.setVisibility(View.INVISIBLE);
        // Finally, show the loading indicator
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the View for the weather data visible and hide the error message and
     * loading indicator.
     */
    private void showWeatherDataRecyclerView() {
        // First, hide the loading indicator
        mProgressBar.setVisibility(View.INVISIBLE);
        // Finally, make sure the weather data is visible
        mRecyclerView.setVisibility(View.VISIBLE);
    }


}
