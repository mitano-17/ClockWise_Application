package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class viewAttendeeForm extends AppCompatActivity {

    private FirebaseFirestore db;
    private String userId, eventId;
    private ArrayList<String> selectedFields;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_form);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.itemRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userId = getIntent().getStringExtra("userId");
        eventId = getIntent().getStringExtra("eventId");
        selectedFields = getIntent().getStringArrayListExtra("selectedFields");

        TextView titleTv = findViewById(R.id.TitleTv);
        titleTv.setText("View Attendee Form");

        loadAttendeeForm();

        FloatingActionButton backBt = findViewById(R.id.floatingActionBack);
        backBt.setOnClickListener(v -> finish());

    }

    private void loadAttendeeForm() {
        db.collection("users").document(userId).collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        AttendeeFormAdapter adapter = new AttendeeFormAdapter(selectedFields);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(this, "No form fields found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load attendee form.", Toast.LENGTH_SHORT).show();
                });
    }

}
