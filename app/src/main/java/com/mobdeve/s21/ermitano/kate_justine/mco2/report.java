package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class report extends AppCompatActivity {

    private ImageView imgBack;
    private TextView totalJoinView, totalEventsView, missedMeetingsView;

    // Firebase instances
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI components
        imgBack = findViewById(R.id.back);
        totalJoinView = findViewById(R.id.totalJoined);
        totalEventsView = findViewById(R.id.totalEvents);
        missedMeetingsView = findViewById(R.id.missedMeetings);

        // Fetch attendance data dynamically using a Firestore listener
        fetchAttendanceData();

        // Back button functionality
        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(report.this, profile.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchAttendanceData() {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            Log.e("ReportActivity", "No logged-in user.");
            return;
        }

        String userId = auth.getCurrentUser().getUid(); // Get current user's UID

        // Attach a realtime listener to the "events" collection
        firestore.collection("users").document(userId).collection("events")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("ReportActivity", "Failed to fetch data: ", error);
                        Toast.makeText(report.this, "Failed to load attendance data.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        int totalJoined = 0;
                        int totalEvents = value.size();
                        int missedMeetings = 0;

                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            Boolean isJoined = documentChange.getDocument().getBoolean("isJoined");
                            String endDateStr = documentChange.getDocument().getString("endDate");

                            // Ensure the event is actually joined
                            if (isJoined != null && isJoined) {
                                totalJoined++;
                                if (isEventMissed(endDateStr)) {
                                    missedMeetings++;
                                }
                            }
                        }

                        updateUI(totalJoined, totalEvents, missedMeetings);
                    }
                });
    }

    private boolean isEventMissed(String endDateStr) {
        if (endDateStr == null || endDateStr.isEmpty()) {
            Log.e("ReportActivity", "Invalid or missing endDate: " + endDateStr);
            return false; // Treat missing or invalid dates as not missed
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila")); // Set to Philippine time zone
        try {
            Date eventEndDate = dateFormat.parse(endDateStr);

            // Get the current time in Philippine time zone
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            currentDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
            Date currentDate = currentDateFormat.parse(currentDateFormat.format(new Date()));

            // Debugging date comparison
            Log.d("ReportActivity", "Event end date (PHT): " + eventEndDate);
            Log.d("ReportActivity", "Current date (PHT): " + currentDate);

            return eventEndDate != null && eventEndDate.before(currentDate);
        } catch (ParseException e) {
            Log.e("ReportActivity", "Invalid date format for event: " + endDateStr, e);
            return false;
        }
    }

    private void updateUI(int totalJoined, int totalEvents, int missedMeetings) {
        // Update TextViews with fetched data
        totalJoinView.setText(String.valueOf(totalJoined));
        totalEventsView.setText(String.valueOf(totalEvents));
        missedMeetingsView.setText(String.valueOf(missedMeetings));
    }
}
