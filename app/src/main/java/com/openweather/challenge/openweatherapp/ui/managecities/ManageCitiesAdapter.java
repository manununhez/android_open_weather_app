package com.openweather.challenge.openweatherapp.ui.managecities;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.openweather.challenge.openweatherapp.R;
import com.openweather.challenge.openweatherapp.databinding.WeatherItemBinding;
import com.openweather.challenge.openweatherapp.db.entity.WeatherEntity;
import com.openweather.challenge.openweatherapp.ui.addcity.SearchWeatherItemClickCallback;
import com.openweather.challenge.openweatherapp.utils.OpenWeatherUtils;

import java.util.List;

public class ManageCitiesAdapter extends RecyclerView.Adapter<ManageCitiesAdapter.ViewHolder> {
//    private final OnItemClickListener itemClickListener;
//    private final OnLongItemClickListener longItemClickListener;
    private final SearchWeatherItemClickCallback mWeatherItemClickCallback;

    private final List<WeatherEntity> mArrayList;
//    private Context mContext;

    public ManageCitiesAdapter(List<WeatherEntity> arrayList, SearchWeatherItemClickCallback itemClickListener) {
        mArrayList = arrayList;
        mWeatherItemClickCallback = itemClickListener;
    }

    @NonNull
    @Override
    public ManageCitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        WeatherItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.weather_item,
                        parent, false);
        binding.setOnClickCallback(mWeatherItemClickCallback);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageCitiesAdapter.ViewHolder viewHolder, int position) {
        viewHolder.binding.setWeatherEntity(mArrayList.get(position));
//        viewHolder.bind(mArrayList.get(position), mWeatherItemClickCallback, mWeatherItemLongClickCallback);

    }


    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

//
//    public interface OnItemClickListener {
//        void onItemClick(WeatherEntity item);
//    }
//
//    public interface OnLongItemClickListener {
//        void onLongItemClick(WeatherEntity item);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final WeatherItemBinding binding;

//        private final TextView tvTemperature;
//        private final TextView tvCityName;
//        private final TextView tvWeatherCondition;
//        private final ImageView ivWeatherCondition;

        ViewHolder(WeatherItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

//            tvCityName = view.findViewById(R.id.tvCityName);
//            tvTemperature = view.findViewById(R.id.tvTemperature);
//            tvWeatherCondition = view.findViewById(R.id.tvWeatherCondition);
//            ivWeatherCondition = view.findViewById(R.id.ivWeatherCondition);
        }

//        void bind(WeatherEntity item, SearchWeatherItemClickCallback itemClickListener, SearchWeatherItemClickCallback longItemClickListener) {
//            int weatherImageId = OpenWeatherUtils.geResourceIdForWeatherCondition(item.getWeather_id());
//
////            tvCityName.setText(mContext.getString(R.string.full_city_name,item.getName(), item.getSys_country()));
////            tvTemperature.setText(OpenWeatherUtils.formatTemperature(mContext, item.getMain_temp()));
////            tvWeatherCondition.setText(item.getWeather_main());
////
////            ivWeatherCondition.setImageResource(weatherImageId);
////
////            itemView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    itemClickListener.onItemClick(item);
////                }
////            });
////
////            itemView.setOnLongClickListener(new View.OnLongClickListener() {
////                @Override
////                public boolean onLongClick(View view) {
////                    longItemClickListener.onLongItemClick(item);
////                    return false;
////                }
////            });
//        }


    }

}
