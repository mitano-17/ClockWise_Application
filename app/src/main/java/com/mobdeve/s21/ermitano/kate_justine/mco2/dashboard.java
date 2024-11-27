package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {

    private RecyclerView eventsRecycler;
    private dashboard_adapter myAdapter;
    private ArrayList<course> courseList;
    private List<Event> eventList = new ArrayList<>();
    private TextView textView; // Greeting TextView

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set to dashboard as default
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Set greeting with full name
        textView = findViewById(R.id.nameGreetTv);
        loadUserName(); // Fetch the full name from Firestore

        // Set layout for RecyclerView
        eventsRecycler = findViewById(R.id.EventsRv);
        eventsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Fetch events from Firestore and populate the RecyclerView
        firestore.collection("events")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Map Firestore documents to Event objects
                        List<Event> events = queryDocumentSnapshots.toObjects(Event.class);

                        // Update the adapter and RecyclerView
                        eventList.clear();
                        eventList.addAll(events);
                        myAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load events: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Set the adapter
        myAdapter = new dashboard_adapter(this, eventList);
        eventsRecycler.setAdapter(myAdapter);


        // Create event button
        Button createBtn = findViewById(R.id.createEventBtn);
        createBtn.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, createEvent.class);
            startActivity(intent);
        });

        // Join event button
        Button joinBtn = findViewById(R.id.joinEventBtn);
        joinBtn.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, joinEventqr.class);
            startActivity(intent);
        });

        // Profile button
        ImageView profileBtn = findViewById(R.id.userTop);
        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, profile.class);
            startActivity(intent);
        });
    }


    private void loadUserName() {
        String userId = auth.getCurrentUser().getUid();

        // Fetch user's fullName from Firestore
        firestore.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Retrieve the full name
                            String fullName = document.getString("fullName");

                            // Set greeting message
                            textView.setText(fullName + "!");
                        } else {
                            textView.setText("Welcome!");
                            Toast.makeText(dashboard.this, "User data not found!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        textView.setText("Welcome!");
                        Toast.makeText(dashboard.this, "Failed to load user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
