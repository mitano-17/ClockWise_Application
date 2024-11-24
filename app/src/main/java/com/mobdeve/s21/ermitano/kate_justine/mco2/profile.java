package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profile extends AppCompatActivity {

    // Declare UI elements
    private ImageView profilePicture, backButton, profileEditIcon;
    private TextView profileName, profileEmail, report, logoutText;
    private Button editProfileBtn;

    // Firebase instances
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI elements
        profilePicture = findViewById(R.id.profile_picture);
        backButton = findViewById(R.id.back);
        profileEditIcon = findViewById(R.id.profile_edit_icon);
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        report = findViewById(R.id.report);
        logoutText = findViewById(R.id.logout);
        editProfileBtn = findViewById(R.id.btnEditProfile);

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Load user profile
        loadUserProfile();

        // Back button functionality
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(profile.this, dashboard.class);
            startActivity(intent);
            finish();
        });

        // Logout button functionality
        logoutText.setOnClickListener(view -> {
            auth.signOut();
            Intent intent = new Intent(profile.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Edit profile button functionality
        editProfileBtn.setOnClickListener(view -> {
            Intent intent = new Intent(profile.this, editprofile.class);
            startActivityForResult(intent, 1); // Use requestCode = 1 to handle result
        });

        // Report button functionality (optional)
        report.setOnClickListener(view -> {
            Intent intent = new Intent(profile.this, report.class);
            startActivity(intent);
        });
    }

    private void loadUserProfile() {
        // Get the current user's UID from Firebase Authentication
        String userId = auth.getCurrentUser().getUid();

        // Fetch the user's data from Firestore
        firestore.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Retrieve fields from Firestore
                            String fullName = document.getString("fullName");
                            String email = document.getString("email");
                            String company = document.getString("company"); // Optional, not used in layout
                            String profilePicUrl = document.getString("profilePic"); // May not exist

                            // Update UI elements
                            profileName.setText(fullName);
                            profileEmail.setText(email);

                            // Log missing profile picture field
                            if (profilePicUrl == null || profilePicUrl.isEmpty()) {
                                Log.d("ProfileActivity", "No profile picture found. Using default.");
                            }

                            // Load profile picture using Glide or use a default placeholder
                            Glide.with(profile.this)
                                    .load(profilePicUrl != null ? profilePicUrl : R.drawable.default_profile)
                                    .placeholder(R.drawable.default_profile)
                                    .into(profilePicture);
                        } else {
                            Toast.makeText(profile.this, "Profile data not found!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(profile.this, "Failed to load profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Refresh profile data if changes were made in EditProfileActivity
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadUserProfile(); // Reload profile details
        }
    }
}
