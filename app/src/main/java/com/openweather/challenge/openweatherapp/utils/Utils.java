package com.openweather.challenge.openweatherapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;

import com.openweather.challenge.openweatherapp.R;

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
                commaSepValueBuilder.append(",");
            }
        }

        return commaSepValueBuilder.toString();

    }


    public static AlertDialog.Builder getAlertDialogWithChoice(Context context, String title, String message, String yesButton, DialogInterface.OnClickListener yesClickListener,
                                                               String noButton, DialogInterface.OnClickListener noClickListener, DialogInterface.OnCancelListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_AppCompat));
        dialog.setTitle(title);
        dialog.setMessage(message);

        dialog.setPositiveButton(yesButton, yesClickListener);
        dialog.setNegativeButton(noButton, noClickListener);

        dialog.setOnCancelListener(listener);
        return dialog;
    }
}
