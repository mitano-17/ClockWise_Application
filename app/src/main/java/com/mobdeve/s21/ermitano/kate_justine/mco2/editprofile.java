package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editprofile extends AppCompatActivity {

    private EditText fullNameEt, companyEt, emailEt;
    private Button saveProfileBtn, cancelProfileBtn;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        // Initialize UI elements
        fullNameEt = findViewById(R.id.fullNameEt);
        companyEt = findViewById(R.id.companyEt);
        emailEt = findViewById(R.id.emailEt);
        saveProfileBtn = findViewById(R.id.saveProfileBtn);
        cancelProfileBtn = findViewById(R.id.cancelProfileBtn);

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Load current profile data
        loadUserProfile();

        // Save profile changes
        saveProfileBtn.setOnClickListener(v -> saveChanges());

        // Cancel editing
        cancelProfileBtn.setOnClickListener(v -> finish());
    }

    private void loadUserProfile() {
        String userId = auth.getCurrentUser().getUid();

        firestore.collection("users").document(userId).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        fullNameEt.setText(document.getString("fullName"));
                        companyEt.setText(document.getString("company"));
                        emailEt.setText(document.getString("email"));
                    } else {
                        Toast.makeText(this, "Profile data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show());
    }

    private void saveChanges() {
        String fullName = fullNameEt.getText().toString();
        String company = companyEt.getText().toString();
        String email = emailEt.getText().toString();

        if (fullName.isEmpty() || company.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        Map<String, Object> updates = new HashMap<>();
        updates.put("fullName", fullName);
        updates.put("company", company);
        updates.put("email", email);

        firestore.collection("users").document(userId).update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                    // Set the result to indicate changes were made
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show());
    }

}
