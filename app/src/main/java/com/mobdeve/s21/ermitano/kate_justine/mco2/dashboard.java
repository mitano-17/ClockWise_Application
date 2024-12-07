package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class dashboard extends AppCompatActivity {

    private TextView greetingTv, nameGreetTv, timeTv;
    private TextView totalJoinedTv, totalEventsTv, missedMeetingsTv;
    private RecyclerView eventsRecycler;
    private dashboard_adapter myAdapter;
    private List<Event> eventList = new ArrayList<>();

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private Handler timeHandler;
    private Runnable timeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI components
        greetingTv = findViewById(R.id.greetingTv);
        nameGreetTv = findViewById(R.id.nameGreetTv);
        timeTv = findViewById(R.id.timeTv);
        totalJoinedTv = findViewById(R.id.totalJoined);
        totalEventsTv = findViewById(R.id.totalEvents);
        missedMeetingsTv = findViewById(R.id.missedMeetings);
        eventsRecycler = findViewById(R.id.EventsRv);

        // Set up RecyclerView
        eventsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        myAdapter = new dashboard_adapter(this, eventList);
        eventsRecycler.setAdapter(myAdapter);

        // Load data dynamically
        startClock();  // Start real-time clock updates
        loadUserName();  // Fetch user name dynamically
        fetchEventStatistics();  // Fetch and update event statistics
        fetchEvents();  // Fetch events dynamically

        // Set up buttons
        setupButtons();
    }

    private void startClock() {
        timeHandler = new Handler();
        timeRunnable = new Runnable() {
            @Override
            public void run() {
                // Update time dynamically
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
                String currentTime = timeFormat.format(new Date());
                timeTv.setText(currentTime);

                // Update greeting based on the time of day
                int hour = new Date().getHours();
                if (hour < 12) {
                    greetingTv.setText("Good morning,");
                } else if (hour < 18) {
                    greetingTv.setText("Good afternoon,");
                } else {
                    greetingTv.setText("Good evening,");
                }

                // Repeat every second
                timeHandler.postDelayed(this, 1000);
            }
        };
        timeHandler.post(timeRunnable);
    }

    private void loadUserName() {
        String userId = auth.getCurrentUser().getUid();

        firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String fullName = document.getString("fullName");
                        nameGreetTv.setText(fullName != null ? fullName + "!" : "Welcome!");
                    } else {
                        nameGreetTv.setText("Welcome!");
                        Toast.makeText(dashboard.this, "User data not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    nameGreetTv.setText("Welcome!");
                    Toast.makeText(dashboard.this, "Failed to load user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchEventStatistics() {
        String userId = auth.getCurrentUser().getUid();

        firestore.collection("users").document(userId).collection("events")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Failed to load statistics: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        int totalJoined = 0;
                        int totalEvents = value.size();
                        int missedMeetings = 0;

                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            Boolean isJoined = documentChange.getDocument().getBoolean("isJoined");
                            String endDateStr = documentChange.getDocument().getString("endDate");

                            if (isJoined != null && isJoined) {
                                totalJoined++;
                                if (isEventMissed(endDateStr)) {
                                    missedMeetings++;
                                }
                            }
                        }

                        updateStatistics(totalJoined, totalEvents, missedMeetings);
                    }
                });
    }

    private boolean isEventMissed(String endDateStr) {
        if (endDateStr == null || endDateStr.isEmpty()) {
            Log.e("Dashboard", "Invalid or missing endDate: " + endDateStr);
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
        try {
            Date eventEndDate = dateFormat.parse(endDateStr);

            // Get the current time in Philippine time zone
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            currentDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
            Date currentDate = currentDateFormat.parse(currentDateFormat.format(new Date()));

            Log.d("Dashboard", "Event end date (PHT): " + eventEndDate);
            Log.d("Dashboard", "Current date (PHT): " + currentDate);

            return eventEndDate != null && eventEndDate.before(currentDate);
        } catch (ParseException e) {
            Log.e("Dashboard", "Invalid date format for event: " + endDateStr, e);
            return false;
        }
    }

    private void updateStatistics(int totalJoined, int totalEvents, int missedMeetings) {
        totalJoinedTv.setText(String.valueOf(totalJoined));
        totalEventsTv.setText(String.valueOf(totalEvents));
        missedMeetingsTv.setText(String.valueOf(missedMeetings));
    }

    private void fetchEvents() {
        String userId = auth.getCurrentUser().getUid();

        firestore.collection("users").document(userId).collection("events")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Failed to load events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        eventList.clear();
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                Event event = documentChange.getDocument().toObject(Event.class);
                                eventList.add(event);
                            }
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void setupButtons() {
        // Create Event Button
        Button createBtn = findViewById(R.id.createEventBtn);
        createBtn.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, createEvent.class);
            startActivity(intent);
        });

        // Join Event Button
        Button joinBtn = findViewById(R.id.joinEventBtn);
        joinBtn.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, joinEventqr.class);
            startActivity(intent);
        });

        // Profile Button
        ImageView profileBtn = findViewById(R.id.userTop);
        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, profile.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the clock when the activity is destroyed
        if (timeHandler != null && timeRunnable != null) {
            timeHandler.removeCallbacks(timeRunnable);
        }
    }
}
