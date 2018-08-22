/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.managecities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.ui.addcity.AddCityActivity;

public class ManageCitiesFragment extends Fragment {

    private ManageCitiesViewModel mViewModel;

    public static ManageCitiesFragment newInstance() {
        return new ManageCitiesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.manage_cities_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true); //search menu functionality

        mViewModel = ViewModelProviders.of(this).get(ManageCitiesViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        menu.findItem(R.id.action_search).setVisible(false);
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
