package com.mobdeve.s21.ermitano.kate_justine.mco2;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;


public class viewEvent extends AppCompatActivity {
    private String eventId;
    private static final int REQUEST_EDIT_EVENT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewevent_layout);

        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");
        String eventName = intent.getStringExtra("eventName");
        String startDate = intent.getStringExtra("startDate");
        String startTime = intent.getStringExtra("startTime");
        String endDate = intent.getStringExtra("endDate");
        String endTime = intent.getStringExtra("endTime");
        String numAtendees = intent.getStringExtra("numAtendees");
        String color = intent.getStringExtra("color");
        String eventTags = intent.getStringExtra("eventTags");

        FloatingActionButton editEventBt = findViewById(R.id.editBtn);
        editEventBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewEvent.this, editEvent.class);
                intent.putExtra("eventId", eventId);
                intent.putExtra("eventName", eventName);
                intent.putExtra("startDate", startDate);
                intent.putExtra("startTime", startTime);
                intent.putExtra("endDate", endDate);
                intent.putExtra("endTime", endTime);
                intent.putExtra("numAtendees", numAtendees);
                intent.putExtra("color", color);
                intent.putExtra("eventTags", eventTags);
                startActivityForResult(intent, REQUEST_EDIT_EVENT);
            }
        });
        FloatingActionButton backBtn = findViewById(R.id.floatingActionBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewEvent.this, dashboard.class);
                startActivity(intent);
            }
        });

        TextView eventNameTv = findViewById(R.id.viewEventTitle);
        TextView startDateTv = findViewById(R.id.viewStartDateTv);
        TextView startTimeTv = findViewById(R.id.viewStartTimeTv);
        TextView endDateTv = findViewById(R.id.viewEventEndDateTv);
        TextView endTimeTv = findViewById(R.id.viewEventEndTimeTv);
        TextView numAtendeesTv = findViewById(R.id.NumTv);
        TextView eventTagsTextView = findViewById(R.id.viewEventTagsTv);
        View colorView = findViewById(R.id.eventBg);

        eventNameTv.setText(eventName);
        startDateTv.setText(startDate);
        startTimeTv.setText(startTime);
        endDateTv.setText(endDate);
        endTimeTv.setText(endTime);
        numAtendeesTv.setText(numAtendees);
        colorView.setBackgroundColor(Color.parseColor(color));
        eventTagsTextView.setText(eventTags);


    }

    // Override onActivityResult to handle the result from editEvent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_EVENT && resultCode == RESULT_OK) {
            // Extract updated data from the Intent
            String updatedEventName = data.getStringExtra("eventName");
            String updatedStartDate = data.getStringExtra("startDate");
            String updatedStartTime = data.getStringExtra("startTime");
            String updatedEndDate = data.getStringExtra("endDate");
            String updatedEndTime = data.getStringExtra("endTime");
            String updatedNumAtendees = data.getStringExtra("numAtendees");
            String updatedColor = data.getStringExtra("color");
            String updatedEventTags = data.getStringExtra("eventTags");

            // Update UI elements with the updated data
            TextView eventNameTv = findViewById(R.id.viewEventTitle);
            TextView startDateTv = findViewById(R.id.viewStartDateTv);
            TextView startTimeTv = findViewById(R.id.viewStartTimeTv);
            TextView endDateTv = findViewById(R.id.viewEventEndDateTv);
            TextView endTimeTv = findViewById(R.id.viewEventEndTimeTv);
            TextView numAtendeesTv = findViewById(R.id.NumTv);
            TextView eventTagsTextView = findViewById(R.id.viewEventTagsTv);
            View colorView = findViewById(R.id.eventBg);

            eventNameTv.setText(updatedEventName);
            startDateTv.setText(updatedStartDate);
            startTimeTv.setText(updatedStartTime);
            endDateTv.setText(updatedEndDate);
            endTimeTv.setText(updatedEndTime);
            numAtendeesTv.setText(updatedNumAtendees);
            colorView.setBackgroundColor(Color.parseColor(updatedColor));
            eventTagsTextView.setText(updatedEventTags);
        }
    }
}
