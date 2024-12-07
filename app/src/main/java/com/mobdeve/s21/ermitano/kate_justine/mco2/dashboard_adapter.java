package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class dashboard_adapter extends RecyclerView.Adapter<dashboard_viewholder> {
    private Context context;
    private List<Event> eventList;

    public dashboard_adapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public dashboard_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new dashboard_viewholder(LayoutInflater.from(context).inflate(R.layout.events_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull dashboard_viewholder holder, int position) {
        Event currentEvent = eventList.get(position);

        // Bind event data
        holder.courseTv.setText(currentEvent.getEventName());
        holder.dayTv.setText(currentEvent.getStartDate() + " - " + currentEvent.getEndDate());
        holder.timeTv.setText(currentEvent.getStartTime() + " - " + currentEvent.getEndTime());
        holder.itemView.setBackgroundColor(Color.parseColor(currentEvent.getColor()));

        // Click listener to navigate to viewEvent
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, viewEvent.class);
            intent.putExtra("eventId", currentEvent.getEventId());
            intent.putExtra("eventName", currentEvent.getEventName());
            intent.putExtra("startDate", currentEvent.getStartDate());
            intent.putExtra("startTime", currentEvent.getStartTime());
            intent.putExtra("endDate", currentEvent.getEndDate());
            intent.putExtra("endTime", currentEvent.getEndTime());
            intent.putExtra("numAttendees", String.valueOf(currentEvent.getNumAttendees()));
            intent.putExtra("color", currentEvent.getColor());
            intent.putExtra("eventType", currentEvent.getEventType());
            intent.putExtra("userId", currentEvent.getUserId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
