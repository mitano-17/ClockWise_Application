package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class viewEvent extends AppCompatActivity {
    private String userId, eventId, eventName, startDate, startTime, endDate, endTime, numAttendees, color, eventTags, qrCodeData, eventLoc;
    private static final int REQUEST_EDIT_EVENT = 1;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewevent_layout);

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Retrieve data passed through intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        eventId = intent.getStringExtra("eventId");
        eventName = intent.getStringExtra("eventName");
        startDate = intent.getStringExtra("startDate");
        startTime = intent.getStringExtra("startTime");
        endDate = intent.getStringExtra("endDate");
        endTime = intent.getStringExtra("endTime");
        numAttendees = intent.getStringExtra("numAttendees");
        eventLoc = intent.getStringExtra("eventLoc");
        color = intent.getStringExtra("color");
        eventTags = intent.getStringExtra("eventType");

        // Display event details
        TextView eventNameTv = findViewById(R.id.viewEventTitle);
        TextView startDateTv = findViewById(R.id.viewStartDateTv);
        TextView startTimeTv = findViewById(R.id.viewStartTimeTv);
        TextView endDateTv = findViewById(R.id.viewEventEndDateTv);
        TextView endTimeTv = findViewById(R.id.viewEventEndTimeTv);
        TextView numAttendeesTv = findViewById(R.id.NumTv);
        TextView eventTagsTextView = findViewById(R.id.viewEventTagsTv);
        View colorView = findViewById(R.id.eventBg);

        // Set event details to UI components
        eventNameTv.setText(eventName);
        startDateTv.setText(startDate);
        startTimeTv.setText(startTime);
        endDateTv.setText(endDate);
        endTimeTv.setText(endTime);
        numAttendeesTv.setText(numAttendees);
        colorView.setBackgroundColor(Color.parseColor(color));
        eventTagsTextView.setText(eventTags);

        // Handle back button click
        FloatingActionButton backBtn = findViewById(R.id.floatingActionBack);
        backBtn.setOnClickListener(v -> {
            Intent backIntent = new Intent(viewEvent.this, dashboard.class);
            startActivity(backIntent);
        });

        // Handle edit event button click
        FloatingActionButton editEventBt = findViewById(R.id.editBtn);
        editEventBt.setOnClickListener(v -> {
            Intent editIntent = new Intent(viewEvent.this, editEvent.class);
            editIntent.putExtra("userId", userId);
            editIntent.putExtra("eventId", eventId);
            editIntent.putExtra("eventName", eventName);
            editIntent.putExtra("startDate", startDate);
            editIntent.putExtra("startTime", startTime);
            editIntent.putExtra("endDate", endDate);
            editIntent.putExtra("endTime", endTime);
            editIntent.putExtra("numAttendees", numAttendees);
            editIntent.putExtra("eventLoc", eventLoc);
            editIntent.putExtra("color", color);
            editIntent.putExtra("eventType", eventTags);
            startActivityForResult(editIntent, REQUEST_EDIT_EVENT);
        });

        // Handle generate QR button click
        Button generateQR = findViewById(R.id.genQRBt);
        generateQR.setOnClickListener(v -> {
            Intent qrIntent = new Intent(viewEvent.this, generatedQRcode.class);
            qrIntent.putExtra("eventId", eventId);
            startActivity(qrIntent);
        });

        // Handle custom attendee form button click
        Button customAttendeeForm = findViewById(R.id.cstmAttendee);
        customAttendeeForm.setOnClickListener(v -> {
            Intent formIntent = new Intent(viewEvent.this, customAttendeeForm.class);
            formIntent.putExtra("userId", userId);
            formIntent.putExtra("eventId", eventId);
            startActivity(formIntent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Update UI with edited event details if editing is successful
        if (requestCode == REQUEST_EDIT_EVENT && resultCode == RESULT_OK && data != null) {
            eventName = data.getStringExtra("eventName");
            startDate = data.getStringExtra("startDate");
            startTime = data.getStringExtra("startTime");
            endDate = data.getStringExtra("endDate");
            endTime = data.getStringExtra("endTime");
            numAttendees = data.getStringExtra("numAttendees");
            eventLoc = data.getStringExtra("eventLoc");
            color = data.getStringExtra("color");
            eventTags = data.getStringExtra("eventType");

            TextView eventNameTv = findViewById(R.id.viewEventTitle);
            TextView startDateTv = findViewById(R.id.viewStartDateTv);
            TextView startTimeTv = findViewById(R.id.viewStartTimeTv);
            TextView endDateTv = findViewById(R.id.viewEventEndDateTv);
            TextView endTimeTv = findViewById(R.id.viewEventEndTimeTv);
            TextView numAttendeesTv = findViewById(R.id.NumTv);
            TextView eventTagsTextView = findViewById(R.id.viewEventTagsTv);
            View colorView = findViewById(R.id.eventBg);

            eventNameTv.setText(eventName);
            startDateTv.setText(startDate);
            startTimeTv.setText(startTime);
            endDateTv.setText(endDate);
            endTimeTv.setText(endTime);
            numAttendeesTv.setText(numAttendees);
            colorView.setBackgroundColor(Color.parseColor(color));
            eventTagsTextView.setText(eventTags);
        }
    }
}
