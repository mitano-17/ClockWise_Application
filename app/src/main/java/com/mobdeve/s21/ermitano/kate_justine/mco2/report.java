package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class report extends AppCompatActivity {

    private ImageView imgBack;
    private TextView totalHoursView, totalEventsView, missedMeetingsView;

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
        totalHoursView = findViewById(R.id.totalHours);
        totalEventsView = findViewById(R.id.totalEvents);
        missedMeetingsView = findViewById(R.id.missedMeetings);

        // Fetch attendance data from Firestore
        fetchAttendanceData();

        // Back button functionality
        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(report.this, profile.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchAttendanceData() {
        String userId = auth.getCurrentUser().getUid(); // Get current user's UID

        firestore.collection("attendance").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        DocumentSnapshot document = task.getResult();

                        // Fetch data from Firestore
                        int totalHours = document.getLong("totalHours").intValue();
                        int totalEvents = document.getLong("totalEvents").intValue();
                        int missedMeetings = document.getLong("missedMeetings").intValue();

                        // Update the UI with fetched data
                        updateUI(totalHours, totalEvents, missedMeetings);
                    } else {
                        Log.e("ReportActivity", "Failed to fetch data: ", task.getException());
                        Toast.makeText(report.this, "Failed to load attendance data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(int totalHours, int totalEvents, int missedMeetings) {
        // Update TextViews with fetched data
        totalHoursView.setText(totalHours + " hrs");
        totalEventsView.setText(String.valueOf(totalEvents));
        missedMeetingsView.setText(String.valueOf(missedMeetings));
    }
}
