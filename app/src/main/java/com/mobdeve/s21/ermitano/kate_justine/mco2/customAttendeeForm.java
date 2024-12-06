package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class customAttendeeForm extends AppCompatActivity {

    FirebaseFirestore db;
    private String eventId;
    private String userId;
    private ArrayList<String> selectedFields;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_attendee_form);

        db = FirebaseFirestore.getInstance();

        eventId = getIntent().getStringExtra("eventId");
        userId = getIntent().getStringExtra("userId");

        LinearLayout checkBoxContainer = findViewById(R.id.formButtons);
        FloatingActionButton createBt = findViewById(R.id.createBt);
        FloatingActionButton backBt = findViewById(R.id.floatingActionBack);

        createBt.setOnClickListener(v -> {
            selectedFields = new ArrayList<>();

            // Gather selected checkboxes
            for (int i = 0; i < checkBoxContainer.getChildCount(); i++) {
                if (checkBoxContainer.getChildAt(i) instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) checkBoxContainer.getChildAt(i);
                    if (checkBox.isChecked()) {
                        selectedFields.add(checkBox.getText().toString());

                    }
                }
            }

            if (selectedFields.isEmpty()) {
                Toast.makeText(this, "Please select at least one field.", Toast.LENGTH_SHORT).show();
                return;
            }

            AttendeeForm attendeeForm = new AttendeeForm(userId, eventId, selectedFields, null);

            db.collection("users").document(userId).collection("events").document(eventId)
                    .collection("attendeeForm").document("formId").get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if(documentSnapshot.exists()){
                            updateAttendeeForm();
                        }else{
                            db.collection("users").document(userId).collection("events").document(eventId)
                                    .collection("attendeeForm").add(attendeeForm)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(this, "Form added successfully!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(this, viewAttendeeForm.class);
                                        intent.putExtra("userId", userId);
                                        intent.putExtra("eventId", eventId);
                                        intent.putStringArrayListExtra("selectedFields", selectedFields);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to add form.", Toast.LENGTH_SHORT).show();
                                    });
                        }

                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to check if form is existing.", Toast.LENGTH_SHORT).show();
                    });
        });

        backBt.setOnClickListener(v -> finish());
    }

    private void updateAttendeeForm() {
        db.collection("users").document(userId).collection("events").document(eventId)
                .update("selectedFields", selectedFields)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Attendee Form Updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update.", Toast.LENGTH_SHORT).show();
                });
    }
}
