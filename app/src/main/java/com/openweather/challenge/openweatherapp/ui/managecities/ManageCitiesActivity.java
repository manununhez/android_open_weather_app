package com.openweather.challenge.openweatherapp.ui.managecities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.ui.addcity.AddCityActivity;
import com.openweather.challenge.openweatherapp.ui.addcity.SearchWeatherItemClickCallback;
import com.openweather.challenge.openweatherapp.utils.InjectorUtils;
import com.openweather.challenge.openweatherapp.utils.Utils;

import java.util.List;
import java.util.Objects;

public class ManageCitiesActivity extends AppCompatActivity implements SearchWeatherItemClickCallback {
    private ManageCitiesViewModel mViewModel;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_cities_activity);

        Objects.requireNonNull(getSupportActionBar()).setTitle("My Cities");  // provide compatibility to all the versions

        //setHasOptionsMenu(true); //search menu functionality

        ManageCitiesViewModelFactory factory = InjectorUtils.provideManageCitiesViewModelFactory(Objects.requireNonNull(this).getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(ManageCitiesViewModel.class);

        initRecyclerView();

        observeCurrentWeathers();

    }

    private void observeCurrentWeathers() {
        mViewModel.getCurrentWeathers().observe(this, weatherEntities -> {
            setRecyclerView(weatherEntities);
        });
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.rvManageCity);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

    }

    private void setRecyclerView(List<WeatherEntity> weatherEntities) {
        ManageCitiesAdapter mAdapter = new ManageCitiesAdapter(weatherEntities, this);

        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onClick(WeatherEntity weatherEntity) {
        //TODO go to specific weather TAB in ShowWeatherActivity

    }

    @Override
    public boolean onLongClick(WeatherEntity weatherEntity) {
        Utils.getAlertDialogWithChoice(this, getString(R.string.title_alert_dialog), getString(R.string.title_delete_alert_dialog),
                getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModel.deleteWeather(weatherEntity);
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
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.search_menu, menu);

        menu.findItem(R.id.action_add_city).setVisible(true);
        menu.findItem(R.id.action_manage_city).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_add_city:
                startActivity(new Intent(this, AddCityActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
