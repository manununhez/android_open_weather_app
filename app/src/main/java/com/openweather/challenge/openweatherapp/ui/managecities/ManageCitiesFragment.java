/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.managecities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.openweather.challenge.openweatherapp.OpenWeatherApp;
import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.entity.CityEntity;
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.ui.addcity.AddCityActivity;
import com.openweather.challenge.openweatherapp.utils.DataFilterSearchAdapter;
import com.openweather.challenge.openweatherapp.utils.InjectorUtils;
import com.openweather.challenge.openweatherapp.utils.ManageCitiesAdapter;
import com.openweather.challenge.openweatherapp.utils.Utils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class ManageCitiesFragment extends Fragment {

    private ManageCitiesViewModel mViewModel;
private View view;
private  RecyclerView mRecyclerView;
private ManageCitiesAdapter mAdapter;
    public static ManageCitiesFragment newInstance() {
        return new ManageCitiesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.manage_cities_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true); //search menu functionality

        ManageCitiesViewModelFactory factory = InjectorUtils.provideManageCitiesViewModelFactory(Objects.requireNonNull(getActivity()).getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(ManageCitiesViewModel.class);

        initRecyclerView();

        mViewModel.getCurrentWeathers().observe(this, weatherEntities -> {
            setRecyclerView(weatherEntities);
        });


    }

    private void initRecyclerView() {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvManageCity);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);

    }

    private void setRecyclerView(List<WeatherEntity> weatherEntities){
        mAdapter = new ManageCitiesAdapter(weatherEntities, new ManageCitiesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WeatherEntity item) {
                //TODO go to specific weather TAB in ShowWeatherActivity
            }
        } , new ManageCitiesAdapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(WeatherEntity item) {
                Utils.getAlertDialogWithChoice(getActivity(), getString(R.string.title_alert_dialog), getString(R.string.title_delete_alert_dialog),
                        getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mViewModel.deleteWeather(item);
                            }
                        }, getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

//        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_add_city).setVisible(true);
        menu.findItem(R.id.action_manage_city).setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_city:
                startActivity(new Intent(getActivity(), AddCityActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
