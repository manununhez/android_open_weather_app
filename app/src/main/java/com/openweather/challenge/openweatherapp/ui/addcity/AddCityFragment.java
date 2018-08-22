/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.addcity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.openweather.challenge.openweatherapp.OpenWeatherApp;
import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.entity.CityEntity;
import com.openweather.challenge.openweatherapp.utils.DataFilterSearchAdapter;
import com.openweather.challenge.openweatherapp.utils.DataSearchAdapter;
import com.openweather.challenge.openweatherapp.utils.InjectorUtils;

import java.util.List;
import java.util.concurrent.Executors;

public class AddCityFragment extends Fragment {

    private AddCityViewModel mViewModel;
    private Button btnTest;
    private TextView tvCountResults;
    private View view;
    private RecyclerView mRecyclerView;
    private DataFilterSearchAdapter mAdapter;


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

        setHasOptionsMenu(true); //search menu functionality
        // Get the ViewModel from the factory
        AddCityViewModelFactory factory = InjectorUtils.provideAddCityViewModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(AddCityViewModel.class);


//        mViewModel.getAllCities().observe(this, cityEntities -> {
//            if (cityEntities != null) OpenWeatherApp.Logger.d(cityEntities.toString());
//        });

        btnTest = (Button) view.findViewById(R.id.btnTest);
        tvCountResults = (TextView) view.findViewById(R.id.tvCountResults);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Remember use a separate thread when you insert elements into database


            }
        });

        initViews();


    }

    private void initViews() {


        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.rvSearch);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);

            List<CityEntity> cityEntities = mViewModel.getAllCities();

            mAdapter = new DataFilterSearchAdapter(cityEntities);
//            mAdapter = new DataSearchAdapter(cityEntities);

            mRecyclerView.setAdapter(mAdapter);

            if(mAdapter != null) tvCountResults.setText(mAdapter.getItemCount() + " results");

        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.action_add_city).setVisible(false);
        menu.findItem(R.id.action_manage_city).setVisible(false);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(onQueryTextListener);

    }


    private SearchView.OnQueryTextListener onQueryTextListener =
            new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    OpenWeatherApp.Logger.d("submitted: " + query);
//                    getDealsFromDb(query);
                    return true;
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
                    if(mAdapter != null) tvCountResults.setText(mAdapter.getItemCount() + " results");
                    return true;
                }

//                private void getDealsFromDb(String searchText) {
//                    searchText = "%"+searchText+"%";
//                    localRepository.getDealsListInfo(DealsSearchActivity.this, searchText)
//                            .observe(DealsSearchActivity.this, new Observer<List<DealInfo>>() {
//                                @Override
//                                public void onChanged(@Nullable List<DealInfo> deals) {
//                                    if (deals == null) {
//                                        return;
//                                    }
//                                    DealsListViewAdapter adapter = new DealsListViewAdapter(
//                                            DealsSearchActivity.this,
//                                            R.layout.deal_item_layout, deals);
//                                    listView.setAdapter(adapter);
//
//                                }
//                            });
//                }
            };
}
