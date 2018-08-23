/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.addcity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
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
import com.openweather.challenge.openweatherapp.entity.CityEntity;
import com.openweather.challenge.openweatherapp.utils.DataFilterSearchAdapter;
import com.openweather.challenge.openweatherapp.utils.InjectorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AddCityFragment extends Fragment implements SearchView.OnQueryTextListener, DataFilterSearchAdapter.OnItemClickListener {

    private AddCityViewModel mViewModel;
    //    private Button btnTest;
    private TextView tvCountResults;
    private View view;
    private RecyclerView mRecyclerView;
    private DataFilterSearchAdapter mAdapter;
    private LiveData<CityEntity> newWeathersFromNetwork;
    private SearchView searchView;
    private List<CityEntity> cityEntities;

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

//        setHasOptionsMenu(true); //search menu functionality
        // Get the ViewModel from the factory
        AddCityViewModelFactory factory = InjectorUtils.provideAddCityViewModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(AddCityViewModel.class);


//        mViewModel.getAllCities().observe(this, cityEntities -> {
//            if (cityEntities != null) OpenWeatherApp.Logger.d(cityEntities.toString());
//        });

//        btnTest = (Button) view.findViewById(R.id.btnTest);
        tvCountResults = (TextView) view.findViewById(R.id.tvCountResults);
        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setQueryHint("Search View");
        searchView.setOnQueryTextListener(this);
//        btnTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Remember use a separate thread when you insert elements into database
//
//
//            }
//        });

        initRecyclerView();


    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvSearch);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        cityEntities = new ArrayList<>();

//            cityEntities.add(new CityEntity(5, "Asuncion", "PY", 123.45, 123.45));
//            cityEntities.add(new CityEntity(6, "Lambare", "UK", 123.45, 123.45));
        mAdapter = new DataFilterSearchAdapter(cityEntities, this);
        mRecyclerView.setAdapter(mAdapter);
        Executors.newSingleThreadScheduledExecutor().execute(() -> {


            cityEntities.addAll(mViewModel.getAllCities());

//            mAdapter = new DataSearchAdapter(cityEntities);

//            mAdapter.notifyDataSetChanged();
            mAdapter = new DataFilterSearchAdapter(cityEntities, this);
            mRecyclerView.setAdapter(mAdapter);

            if (mAdapter != null) tvCountResults.setText(mAdapter.getItemCount() + " results");

        });

//        cityEntities.add(new CityEntity(7, "Warsaw", "PY", 123.45, 123.45));
//        cityEntities.add(new CityEntity(8, "Berlin", "UK", 123.45, 123.45));
//        mAdapter.notifyDataSetChanged();
//
//        new RetrieveAllCities(mViewModel, cityEntities, mAdapter).execute();
//        mRecyclerView.getAdapter().notifyDataSetChanged();

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
//                    OpenWeatherApp.Logger.d(newText);
//                    newText = "%" + newText + "%";
            //List<CityEntity> cityEntities = mViewModel.getCitiesByName(newText);
            mAdapter.getFilter().filter(newText);
            //mAdapter.setItemList(cityEntities);
        }
        if (mAdapter != null)
            tvCountResults.setText(mAdapter.getItemCount() + " results");
        return false;
    }

    //FilterClick
    @Override
    public void onItemClick(CityEntity item) {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            // Insert our new weather data into Sunshine's database
            mViewModel.insertWeather(String.valueOf(item.getId()));
            OpenWeatherApp.Logger.d("New values inserted");
            getActivity().finish();

        });
    }

//    private static class RetrieveAllCities extends AsyncTask<Void, Void, List<CityEntity>> {
//        AddCityViewModel viewModel;
//        List<CityEntity> cityEntities;
//        DataFilterSearchAdapter adapter;
//
//        // only retain a weak reference to the activity
//        RetrieveAllCities(AddCityViewModel mViewModel, List<CityEntity> mCityEntities, DataFilterSearchAdapter mAdapter) {
//            viewModel = mViewModel;
//            adapter = mAdapter;
//            cityEntities = mCityEntities;
//        }
//
//        @Override
//        protected List<CityEntity> doInBackground(Void... voids) {
//            return viewModel.getAllCities();
//        }
//
//        @Override
//        protected void onPostExecute(List<CityEntity> mCityEntities) {
//
//            cityEntities.addAll(mCityEntities);
//            adapter.notifyDataSetChanged();
//        }
//    }
}
