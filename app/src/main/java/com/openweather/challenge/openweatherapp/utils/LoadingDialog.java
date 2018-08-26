
package com.openweather.challenge.openweatherapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import com.openweather.challenge.openweatherapp.R;

import java.util.Objects;

/**
 * Created by gbogarin on 30/11/2015.
 */
public class LoadingDialog extends Dialog {

    private final String message;

    public LoadingDialog(Context context, String message) {
        super(context);
        this.message = message == null ? "" : message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.progress_layout, null);
        TextView text = view.findViewById(R.id.myTextProgress);
        text.setText(message);


//        ProgressBar progressBar = new ProgressBar(getContext());
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

}
