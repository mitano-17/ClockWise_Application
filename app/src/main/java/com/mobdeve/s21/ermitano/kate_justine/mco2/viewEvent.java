package com.mobdeve.s21.ermitano.kate_justine.mco2;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;


public class viewEvent extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewevent_layout);
        FloatingActionButton backBtn = findViewById(R.id.floatingActionBack);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(viewEvent.this, dashboard.class);
                    startActivity(intent);
                }
            });

            Intent intent = getIntent();
            String eventName = intent.getStringExtra("eventName");
            String startDate = intent.getStringExtra("startDate");
            String startTime = intent.getStringExtra("startTime");
            String endDate = intent.getStringExtra("endDate");
            String endTime = intent.getStringExtra("endTime");
            String numAtendees = intent.getStringExtra("numAtendees");
            String color = intent.getStringExtra("color");
            String eventTags = intent.getStringExtra("eventTags");

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

        FloatingActionButton editEventBt = findViewById(R.id.editBtn);
        editEventBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewEvent.this, editEvent.class);

                intent.putExtra("eventName", eventName);
                intent.putExtra("startDate", startDate);
                intent.putExtra("startTime", startTime);
                intent.putExtra("endDate", endDate);
                intent.putExtra("endTime", endTime);
                intent.putExtra("numAtendees", numAtendees);
                intent.putExtra("color", color);
                intent.putExtra("eventTags", eventTags);

                startActivity(intent);
            }
        });

    }
}
