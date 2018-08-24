package com.openweather.challenge.openweatherapp.ui.addcity;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.utils.OpenWeatherUtils;

import java.util.List;

public class AddCitySearchAdapter extends RecyclerView.Adapter<AddCitySearchAdapter.ViewHolder> {
    private final OnItemClickListener mListener;

    private final List<WeatherEntity> mArrayList;
    private Context mContext;

    public AddCitySearchAdapter(List<WeatherEntity> arrayList, OnItemClickListener listener) {
        mArrayList = arrayList;
        mListener = listener;

    }

    @Override
    public AddCitySearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_list_row, viewGroup, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddCitySearchAdapter.ViewHolder viewHolder, int position) {

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


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTemperature;
        private final TextView tvCityName;
        private final TextView tvWeatherCondition;
        private final ImageView ivWeatherCondition;

        ViewHolder(View view) {
            super(view);

            tvCityName = view.findViewById(R.id.tvCityName);
            tvTemperature = view.findViewById(R.id.tvTemperature);
            tvWeatherCondition = view.findViewById(R.id.tvWeatherCondition);
            ivWeatherCondition = view.findViewById(R.id.ivWeatherCondition);
        }

        void bind(WeatherEntity item, OnItemClickListener itemClickListener) {
            int weatherImageId = OpenWeatherUtils.geResourceIdForWeatherCondition(item.getWeather_id());

            tvCityName.setText(item.getName() + ", " + item.getSys_country());
            tvTemperature.setText(OpenWeatherUtils.formatTemperature(mContext, item.getMain_temp()));
            tvWeatherCondition.setText(item.getWeather_main());

            ivWeatherCondition.setImageResource(weatherImageId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(item);

                }
            });


        }


    }


}
