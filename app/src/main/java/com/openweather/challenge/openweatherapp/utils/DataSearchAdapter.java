/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.utils;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.entity.CityEntity;

import java.util.ArrayList;
import java.util.List;

public class DataSearchAdapter extends RecyclerView.Adapter<DataSearchAdapter.ViewHolder> {
    private List<CityEntity> mArrayList;
//    private List<CityEntity> mFilteredList;

    public DataSearchAdapter(List<CityEntity> arrayList) {
        mArrayList = arrayList;
//        mFilteredList = arrayList;
    }

    @Override
    public DataSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataSearchAdapter.ViewHolder viewHolder, int i) {

        viewHolder.textSearch.setText(mArrayList.get(i).getName()+","+mArrayList.get(i).getCountry());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public void setItemList(List<CityEntity> cityEntities) {
        mArrayList.clear();
        mArrayList.addAll(cityEntities);

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textSearch;
        public ViewHolder(View view) {
            super(view);

            textSearch = (TextView)view.findViewById(R.id.textSearch);
        }
    }

}
