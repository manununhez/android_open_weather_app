package com.openweather.challenge.openweatherapp.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.openweather.challenge.openweatherapp.R;

import org.json.JSONObject;


/**
 * Created by Manuel on 11/30/2015.
 */
class VolleyErrorHelper {
    /**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @param context
     * @return
     */
    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.timeout_server_error);
        } else if (isServerProblem(error)) {
            return handleServerError(error, context);
        } else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.no_network_error);
        }
        return context.getResources().getString(R.string.defaultErrorMessage);
    }

    /**
     * Determines whether the error is related to network
     *
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    /**
     * Determines whether the error is related to server
     *
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    /**
     * Handles the server error, tries to determine whether to show a stock message or to
     * show a message retrieved from the server.
     *
     * @param err
     * @param context
     * @return
     */
    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) {
            switch (response.statusCode) {
                case 400:
                case 404:
                case 422:
                case 401:
                    try {
                        // server might return error like this { "error": "Some error occured" }
                        // Use "Gson" to parse the result
                        JSONObject jsonObject = new JSONObject(new String(response.data));
                        String result = jsonObject.getString("error");

                        if (result != null)
                            if (!result.isEmpty())
                                return result;


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // invalid request
                    return error.getMessage();

                default:
                    return context.getResources().getString(R.string.generic_server_down);
            }
        }
        return context.getResources().getString(R.string.defaultErrorMessage);
    }
}
