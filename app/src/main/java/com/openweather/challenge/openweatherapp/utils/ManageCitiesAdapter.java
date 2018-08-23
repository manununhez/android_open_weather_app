/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.utils;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;

import java.util.List;

public class ManageCitiesAdapter extends RecyclerView.Adapter<ManageCitiesAdapter.ViewHolder> {
    private final OnItemClickListener itemClickListener;
    private final OnLongItemClickListener longItemClickListener;
    private List<WeatherEntity> mArrayList;
    private Context mContext;

    public ManageCitiesAdapter(List<WeatherEntity> arrayList, OnItemClickListener listener, OnLongItemClickListener mLongListener) {
        mArrayList = arrayList;
        itemClickListener = listener;
        longItemClickListener = mLongListener;
    }

    @Override
    public ManageCitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_list_row, viewGroup, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ManageCitiesAdapter.ViewHolder viewHolder, int position) {
        viewHolder.bind(mArrayList.get(position), itemClickListener, longItemClickListener);

    }


    @Override
    public int getItemCount() {
        return mArrayList.size();
    }



    public interface OnItemClickListener {
        void onItemClick(WeatherEntity item);
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(WeatherEntity item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTemperature;
        private TextView tvCityName;
        private TextView tvWeatherCondition;

        public ViewHolder(View view) {
            super(view);

            tvCityName = (TextView) view.findViewById(R.id.tvCityName);
            tvTemperature = (TextView) view.findViewById(R.id.tvTemperature);
            tvWeatherCondition = (TextView) view.findViewById(R.id.tvWeatherCondition);
        }

        public void bind(WeatherEntity item, OnItemClickListener itemClickListener, OnLongItemClickListener longItemClickListener) {

            tvCityName.setText(item.getName());
            tvTemperature.setText(OpenWeatherUtils.formatTemperature(mContext, item.getMain_temp()));
            tvWeatherCondition.setText(OpenWeatherUtils.getStringForWeatherCondition(mContext, item.getCod()));


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(mContext, item.getName() + " id=" + item.getId(), Toast.LENGTH_SHORT).show();

                    itemClickListener.onItemClick(item);
                    Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();


                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longItemClickListener.onLongItemClick(item);
                    Toast.makeText(mContext, "LongClick", Toast.LENGTH_SHORT).show();

                    return false;
                }
            });
        }


    }

}
