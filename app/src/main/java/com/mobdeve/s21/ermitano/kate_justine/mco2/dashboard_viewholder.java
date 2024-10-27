package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import androidx.core.view.ViewCompat;

public class dashboard_viewholder extends RecyclerView.ViewHolder{

    //Grab the views from our item_layout file
    TextView courseTv;
    TextView dayTv;
    TextView timeTv;
    View rootView;

    public dashboard_viewholder(@NonNull View itemView) {
        super(itemView);

        courseTv = itemView.findViewById(R.id.courseTv);
        dayTv = itemView.findViewById(R.id.dayTv);
        timeTv = itemView.findViewById(R.id.timeTv);
        rootView = itemView;

    }
}
