/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.addcity;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.utils.OpenWeatherUtils;

import java.util.List;

public class DataSearchAdapter extends RecyclerView.Adapter<DataSearchAdapter.ViewHolder> {
    private final OnItemClickListener mListener;

    private List<WeatherEntity> mArrayList;
//    private List<CityEntity> mFilteredList;
private Context mContext;

    public DataSearchAdapter(List<WeatherEntity> arrayList, OnItemClickListener listener) {
        mArrayList = arrayList;
//        mFilteredList = arrayList;
        mListener = listener;

    }

    @Override
    public DataSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_list_row, viewGroup, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataSearchAdapter.ViewHolder viewHolder, int position) {

        viewHolder.bind(mArrayList.get(position), mListener);

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public void setItemList(List<WeatherEntity> weatherEntities) {
        mArrayList.clear();
        mArrayList.addAll(weatherEntities);

        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(WeatherEntity item);
    }

//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private TextView textSearch;
//
//        public ViewHolder(View view) {
//            super(view);
//
//            textSearch = (TextView) view.findViewById(R.id.textSearch);
//        }
//
//        public void bind(WeatherEntity item, OnItemClickListener mListener) {
//
//            textSearch.setText(item.getName() + "," + item.getSys_country());
//
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(mContext, item.getName() + " id=" + item.getId(), Toast.LENGTH_SHORT).show();
//
//                    mListener.onItemClick(item);
//                }
//            });
//        }
//
//
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTemperature;
        private TextView tvCityName;
        private TextView tvWeatherCondition;
        private ImageView ivWeatherCondition;

        public ViewHolder(View view) {
            super(view);

            tvCityName = (TextView) view.findViewById(R.id.tvCityName);
            tvTemperature = (TextView) view.findViewById(R.id.tvTemperature);
            tvWeatherCondition = (TextView) view.findViewById(R.id.tvWeatherCondition);
            ivWeatherCondition = (ImageView) view.findViewById(R.id.ivWeatherCondition);
        }

        public void bind(WeatherEntity item, OnItemClickListener itemClickListener) {
            int weatherImageId = OpenWeatherUtils.geResourceIdForWeatherCondition(item.getWeather_id());

            tvCityName.setText(item.getName()+", "+item.getSys_country());
            tvTemperature.setText(OpenWeatherUtils.formatTemperature(mContext, item.getMain_temp()));
            tvWeatherCondition.setText(item.getWeather_main());

            ivWeatherCondition.setImageResource(weatherImageId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(mContext, item.getName() + " id=" + item.getId(), Toast.LENGTH_SHORT).show();

                    mListener.onItemClick(item);
//                    Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();


                }
            });


        }


    }


}
