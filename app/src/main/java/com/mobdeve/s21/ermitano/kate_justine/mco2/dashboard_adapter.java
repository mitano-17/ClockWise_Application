package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
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
        course currentCourse = course.get(position);

        holder.courseTv.setText(currentCourse.getCourseTitle());
        holder.dayTv.setText(currentCourse.getCourseDay());
        holder.timeTv.setText(currentCourse.getCourseTime());
        String colorHex = currentCourse.getCourseColour();
        holder.itemView.setBackgroundColor(Color.parseColor(colorHex));

        // Set click listener for each card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check the course title and navigate to the appropriate activity
                if ("GELITPH".equals(currentCourse.getCourseTitle())) {
                    Intent intent = new Intent(context, attendance.class);
                    intent.putExtra("courseTitle", currentCourse.getCourseTitle()); // Pass any necessary data
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return course.size();
    }
}
