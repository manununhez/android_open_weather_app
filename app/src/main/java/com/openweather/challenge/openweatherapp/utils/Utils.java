/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.utils;

import java.util.List;

/**
 * Created by manuel on 23,August,2018
 */
public class Utils {

    /**
     * Use in {@link com.openweather.challenge.openweatherapp.network.NetworkDataSource} to make fetch groups of forecasts by list of citiesID
     * @param list
     * @return Concatenate the elements of a List into a comma separated string
     */
    public  static String listToCommaValues(List<Integer> list){

        //The string builder used to construct the string
        StringBuilder commaSepValueBuilder = new StringBuilder();

        //Looping through the list
        for ( int i = 0; i< list.size(); i++){
            //append the value into the builder
            commaSepValueBuilder.append(list.get(i));

            //if the value is not the last element of the list
            //then append the comma(,) as well
            if ( i != list.size()-1){
                commaSepValueBuilder.append(", ");
            }
        }

        return commaSepValueBuilder.toString();

    }
}
