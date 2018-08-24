package com.openweather.challenge.openweatherapp.ui.addcity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
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
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.utils.InjectorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AddCityFragment extends Fragment implements SearchView.OnQueryTextListener, DataSearchAdapter.OnItemClickListener {

    private AddCityViewModel mViewModel;
    //    private Button btnTest;
    private TextView tvCountResults;
    private View view;
    private RecyclerView mRecyclerView;
    private DataSearchAdapter mAdapter;
    private LiveData<WeatherEntity> weatherSearchByNameResponse;
    private SearchView searchView;
    private List<WeatherEntity> weatherEntities;
    private List<String> queriesHistory;

    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_city_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get the ViewModel from the factory
        AddCityViewModelFactory factory = InjectorUtils.provideAddCityViewModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(AddCityViewModel.class);

        tvCountResults = view.findViewById(R.id.tvCountResults);

        searchView = view.findViewById(R.id.searchView);
        searchView.setQueryHint("Search City");
        searchView.setOnQueryTextListener(this);

        queriesHistory = new ArrayList<>();

        initRecyclerView();


    }

    private void initRecyclerView() {
        mRecyclerView = view.findViewById(R.id.rvSearch);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        weatherEntities = new ArrayList<>();

        mAdapter = new DataSearchAdapter(weatherEntities, this);
        mRecyclerView.setAdapter(mAdapter);

        weatherSearchByNameResponse = mViewModel.getResponseWeatherByName();
        weatherSearchByNameResponse.observeForever(response -> {
            OpenWeatherApp.Logger.d("AddCityFragment = " + response.toString());
            //TODO this can be improved
            if (queriesHistory.size() > 0) { // If at least one query was made, this response is correct. This avoid to have elements in the recyclerView from previous searches.
                weatherEntities.add(response);

                mAdapter.notifyDataSetChanged();
                if (mAdapter != null) tvCountResults.setText(mAdapter.getItemCount() + " results");
            }
        });


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
                mViewModel.getWeatherByName(allRemoved);
                queriesHistory.add(allRemoved);
            }

        } else {
            weatherEntities.clear();
            mAdapter.notifyDataSetChanged();
            if (mAdapter != null) tvCountResults.setText(mAdapter.getItemCount() + " results");

        }
        return false;
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
