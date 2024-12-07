package com.mobdeve.s21.ermitano.kate_justine.mco2;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;


public class viewEvent extends AppCompatActivity {
    private String userId, eventId, eventName, startDate, startTime, endDate, endTime, numAttendees, color, eventTags, qrCodeData;
    private static final int REQUEST_EDIT_EVENT = 1;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewevent_layout);

        Button cstmBt = findViewById(R.id.cstmAttendee);

        //Initialize Firestore instance
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //retrieve data passed through intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        eventId = intent.getStringExtra("eventId");
        eventName = intent.getStringExtra("eventName");
        startDate = intent.getStringExtra("startDate");
        startTime = intent.getStringExtra("startTime");
        endDate = intent.getStringExtra("endDate");
        endTime = intent.getStringExtra("endTime");
        numAttendees = intent.getStringExtra("numAttendees");
        color = intent.getStringExtra("color");
        eventTags = intent.getStringExtra("eventType");
        qrCodeData = intent.getStringExtra("qrCodeData");

        //set editEvent with its click listener
        FloatingActionButton editEventBt = findViewById(R.id.editBtn);
        editEventBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //event details from created event
                Intent intent = new Intent(viewEvent.this, editEvent.class);
                intent.putExtra("userId", userId);
                intent.putExtra("eventId", eventId);
                intent.putExtra("eventName", eventName);
                intent.putExtra("startDate", startDate);
                intent.putExtra("startTime", startTime);
                intent.putExtra("endDate", endDate);
                intent.putExtra("endTime", endTime);
                intent.putExtra("numAttendees", numAttendees);
                intent.putExtra("color", color);
                intent.putExtra("eventType", eventTags);
                startActivityForResult(intent, REQUEST_EDIT_EVENT);
            }
        });

        //click listener for generating qr button
        Button generateQR = findViewById(R.id.genQRBt);
        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewEvent.this, generatedQRcode.class);
                intent.putExtra("eventId", eventId);
                startActivity(intent);;
            }
        });

        //set back button with its click listener
        FloatingActionButton backBtn = findViewById(R.id.floatingActionBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewEvent.this, dashboard.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        cstmBt.setOnClickListener(v ->{
            String userId = auth.getCurrentUser().getUid();

            Intent vintent = new Intent(this, customAttendeeForm.class);

            vintent.putExtra("userId", userId);
            vintent.putExtra("eventId", eventId);

            startActivity(vintent);
        });

        //display event details
        TextView eventNameTv = findViewById(R.id.viewEventTitle);
        TextView startDateTv = findViewById(R.id.viewStartDateTv);
        TextView startTimeTv = findViewById(R.id.viewStartTimeTv);
        TextView endDateTv = findViewById(R.id.viewEventEndDateTv);
        TextView endTimeTv = findViewById(R.id.viewEventEndTimeTv);
        TextView numAttendeesTv = findViewById(R.id.NumTv);
        TextView eventTagsTextView = findViewById(R.id.viewEventTagsTv);
        View colorView = findViewById(R.id.eventBg);

        // set event details to textViews
        eventNameTv.setText(eventName);
        startDateTv.setText(startDate);
        startTimeTv.setText(startTime);
        endDateTv.setText(endDate);
        endTimeTv.setText(endTime);
        numAttendeesTv.setText(numAttendees);
        colorView.setBackgroundColor(Color.parseColor(color));
        eventTagsTextView.setText(eventTags);

        //checks if event exists in the database
        eventExists();
    }

    //checks if event exists in the database
    private void eventExists(){
        db.collection("users").document(userId).collection("events").document(eventId)
                .get().addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        //if exists, then display updated data
                        displayResult(documentSnapshot);
                    }else{
                        // if not, then show deletion message
                        showEventDeleteMsg();
                    }
                }).addOnFailureListener(e ->{
                    // if failed, then show failure message
                    Toast.makeText(this, "Failed to check if event exists.", Toast.LENGTH_SHORT).show();
                });
    }
    //event deletion message
    private void showEventDeleteMsg(){
        Toast.makeText(viewEvent.this, "This event has been deleted.", Toast.LENGTH_LONG).show();
        Intent intent= new Intent(viewEvent.this, dashboard.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
        finish();
    }


    //handles the results of editing the event
    private void displayResult(DocumentSnapshot documentSnapshot){
        Intent data = new Intent();
        data.putExtra("eventName", documentSnapshot.getString("eventName"));
        data.putExtra("startDate", documentSnapshot.getString("startDate"));
        data.putExtra("startTime", documentSnapshot.getString("startTime"));
        data.putExtra("endDate", documentSnapshot.getString("endDate"));
        data.putExtra("endTime", documentSnapshot.getString("endTime"));
        data.putExtra("numAttendees", documentSnapshot.getString("numAttendees"));
        data.putExtra("color", documentSnapshot.getString("color"));
        data.putExtra("eventType", documentSnapshot.getString("eventType"));

        onActivityResult(REQUEST_EDIT_EVENT, RESULT_OK, data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        eventExists();
        if (requestCode == REQUEST_EDIT_EVENT && resultCode == RESULT_OK && data != null) {
            // Extract updated data from the Intent
            String updatedEventName = data.getStringExtra("eventName");
            String updatedStartDate = data.getStringExtra("startDate");
            String updatedStartTime = data.getStringExtra("startTime");
            String updatedEndDate = data.getStringExtra("endDate");
            String updatedEndTime = data.getStringExtra("endTime");
            String updatedNumAttendees = data.getStringExtra("numAttendees");
            String updatedColor = data.getStringExtra("color");
            String updatedEventTags = data.getStringExtra("eventType");

            // Update UI elements with the updated data
            TextView eventNameTv = findViewById(R.id.viewEventTitle);
            TextView startDateTv = findViewById(R.id.viewStartDateTv);
            TextView startTimeTv = findViewById(R.id.viewStartTimeTv);
            TextView endDateTv = findViewById(R.id.viewEventEndDateTv);
            TextView endTimeTv = findViewById(R.id.viewEventEndTimeTv);
            TextView numAttendeesTv = findViewById(R.id.NumTv);
            TextView eventTagsTextView = findViewById(R.id.viewEventTagsTv);
            View colorView = findViewById(R.id.eventBg);

            eventNameTv.setText(updatedEventName);
            startDateTv.setText(updatedStartDate);
            startTimeTv.setText(updatedStartTime);
            endDateTv.setText(updatedEndDate);
            endTimeTv.setText(updatedEndTime);
            numAttendeesTv.setText(updatedNumAttendees);
            colorView.setBackgroundColor(Color.parseColor(updatedColor));
            eventTagsTextView.setText(updatedEventTags);

        }
    }



}
