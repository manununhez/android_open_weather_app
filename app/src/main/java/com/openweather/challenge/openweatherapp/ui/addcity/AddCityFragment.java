package com.openweather.challenge.openweatherapp.ui.addcity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openweather.challenge.openweatherapp.OpenWeatherApp;
import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.ui.showweather.ShowWeatherActivity;
import com.openweather.challenge.openweatherapp.utils.InjectorUtils;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class AddCityFragment extends Fragment implements SearchView.OnQueryTextListener, AddCitySearchAdapter.OnItemClickListener {

    static final String QUERY = "query_search";
    static final String WEATHER_ENTITIES = "weather_entities_search";
    private AddCityViewModel mViewModel;
    //    private Button btnTest;
    private TextView tvCountResults;
    private View rootView;
    private RecyclerView mRecyclerView;
    private AddCitySearchAdapter mAdapter;
    private LiveData<WeatherEntity> weatherSearchByNameResponse;
    private SearchView searchView;
    private ArrayList<WeatherEntity> weatherEntities;
    private ArrayList<String> queriesHistory;

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
    public void onSaveInstanceState(Bundle savedInstanceState) {
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
                savedInstanceState.getParcelableArrayList(WEATHER_ENTITIES) != null) { //In this way, after onDestroyView() might happen and restart, we will sure to init with variables not null
            // Restore value of members from saved state
            queriesHistory = savedInstanceState.getStringArrayList(QUERY);
            weatherEntities = savedInstanceState.getParcelableArrayList(WEATHER_ENTITIES);
        } else {
            // Probably initialize members with default values for a new instance
            queriesHistory = new ArrayList<>();
            weatherEntities = new ArrayList<>();

        }


        // Get the ViewModel from the factory
        AddCityViewModelFactory factory = InjectorUtils.provideAddCityViewModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(AddCityViewModel.class);

        tvCountResults = rootView.findViewById(R.id.tvCountResults);

        searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);


        initRecyclerView();


    }


    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.rvSearch);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new AddCitySearchAdapter(weatherEntities, this);
        mRecyclerView.setAdapter(mAdapter);

        weatherSearchByNameResponse = mViewModel.responseWeatherByCityName();
        weatherSearchByNameResponse.observeForever(response -> {
            OpenWeatherApp.Logger.d("AddCityFragment = " + response.toString());
            //TODO this can be improved
            if (queriesHistory != null && !queriesHistory.isEmpty()) { // If at least one query was made, this response is correct. This avoid to have elements in the recyclerView from previous searches.
                if (!findItemInTheList(weatherEntities, response)) { //We only add a new element if it does not exist in the list yet.
                    weatherEntities.add(response);

                    mAdapter.notifyDataSetChanged();
                    if (mAdapter != null)
                        tvCountResults.setText(mAdapter.getItemCount() + " results");

                }
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
                mViewModel.getWeatherByCityName(allRemoved);
                queriesHistory.add(allRemoved);
            }

        } else {
            weatherEntities.clear();
            mAdapter.notifyDataSetChanged();
            if (mAdapter != null) tvCountResults.setText(mAdapter.getItemCount() + " results");

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

            getActivity().finish();

        });
    }

}
