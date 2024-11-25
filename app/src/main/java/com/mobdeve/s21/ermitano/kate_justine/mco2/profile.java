package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Collections;

public class profile extends AppCompatActivity {

    // UI elements
    private ImageView profilePicture, backButton, profileEditIcon;
    private TextView profileName, profileEmail, report, logoutText;
    private Button editProfileBtn;

    // Firebase instances
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    // Constants
    private static final int PICK_IMAGE_REQUEST = 101;
    private Uri imageUri;

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
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Load user profile
        loadUserProfile();

        // Back button functionality
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(profile.this, dashboard.class);
            startActivity(intent);
            finish();
        });

        // Logout functionality
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

        // Report button functionality
        report.setOnClickListener(view -> {
            Intent intent = new Intent(profile.this, report.class);
            startActivity(intent);
        });

        // Profile picture edit functionality
        profileEditIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    private void loadUserProfile() {
        String userId = auth.getCurrentUser().getUid();

        firestore.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String fullName = document.getString("fullName");
                            String email = document.getString("email");
                            String profilePicUrl = document.getString("profilePic");

                            profileName.setText(fullName);
                            profileEmail.setText(email);

                            Glide.with(profile.this)
                                    .load(profilePicUrl != null ? profilePicUrl : R.drawable.default_profile)
                                    .placeholder(R.drawable.default_profile)
                                    .skipMemoryCache(true)
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Log.d("ProfileActivity", "Image URI: " + imageUri.toString());
            uploadImageToFirebase();
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            loadUserProfile();
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            String userId = auth.getCurrentUser().getUid();
            StorageReference fileReference = storageReference.child("profile_pictures/" + userId + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String downloadUrl = uri.toString();
                                    Log.d("ProfileActivity", "Image uploaded successfully: " + downloadUrl);
                                    updateFirestoreWithProfilePicUrl(downloadUrl);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("ProfileActivity", "Failed to get download URL: " + e.getMessage());
                                    Toast.makeText(profile.this, "Failed to get image URL!", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ProfileActivity", "Image upload failed: " + e.getMessage());
                        Toast.makeText(profile.this, "Failed to upload image!", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(profile.this, "No image selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFirestoreWithProfilePicUrl(String downloadUrl) {
        String userId = auth.getCurrentUser().getUid();

        firestore.collection("users").document(userId)
                .update("profilePic", downloadUrl) // Add or update the `profilePic` field
                .addOnSuccessListener(aVoid -> {
                    Log.d("ProfileActivity", "Profile picture updated in Firestore.");
                    Toast.makeText(profile.this, "Profile picture updated successfully!", Toast.LENGTH_SHORT).show();
                    loadUserProfile();
                })
                .addOnFailureListener(e -> {
                    Log.e("ProfileActivity", "Failed to update Firestore: " + e.getMessage());
                    Toast.makeText(profile.this, "Failed to update profile picture!", Toast.LENGTH_SHORT).show();
                });
    }
}
