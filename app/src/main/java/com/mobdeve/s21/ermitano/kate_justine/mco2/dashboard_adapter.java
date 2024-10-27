package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class dashboard_adapter extends RecyclerView.Adapter<dashboard_viewholder>{
    private Context context;
    private ArrayList<course> course;

    public dashboard_adapter(Context context, ArrayList<course> course) {
        this.context = context;
        this.course = course;
    }

    @NonNull
    @Override
    public dashboard_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new dashboard_viewholder(LayoutInflater.from(context).inflate(R.layout.events_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull dashboard_viewholder holder, int position) {
        holder.courseTv.setText(course.get(position).getCourseTitle());
        holder.dayTv.setText(course.get(position).getCourseDay());
        holder.timeTv.setText(course.get(position).getCourseTime());
    }

    @Override
    public int getItemCount() {
        return course.size();
    }
}
