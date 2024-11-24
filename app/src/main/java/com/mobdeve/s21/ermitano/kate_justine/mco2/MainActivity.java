package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // Add Firebase Authentication instance
    private FirebaseAuth auth;
    // Google Sign-In client
    private GoogleSignInClient gClient;
    private GoogleSignInOptions gOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set to welcome layout as default
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        // Initialize Firebase Authentication instance
        auth = FirebaseAuth.getInstance();

        // Initialize Google Sign-In options
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);

        // Check if user is already logged in with Google
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (gAccount != null) {
            // User is logged in with Google, navigate to the main dashboard
            Intent intent = new Intent(MainActivity.this, dashboard.class);
            startActivity(intent);
            finish(); // Prevent user from going back to the intro screen
        }

        // Register button click listener
        Button registerActivity = findViewById(R.id.SignUpBtn);
        registerActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to registration page
                registerActivity();
            }
        });

        // Login button click listener
        Button loginActivity = findViewById(R.id.loginBtn);
        loginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to login page
                loginActivity();
            }
        });

        // Logout button for users who are logged in
        Button logoutBtn = findViewById(R.id.loginBtn);
        logoutBtn.setVisibility(View.GONE); // Hide logout button initially
        if (auth.getCurrentUser() != null || gAccount != null) {
            // User is logged in via Firebase or Google, show logout button
            logoutBtn.setVisibility(View.VISIBLE);
            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle logout for both Firebase and Google Sign-In
                    auth.signOut(); // Log out from Firebase
                    gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Successfully logged out, navigate back to intro screen
                            finish();
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                        }
                    });
                }
            });
        }
    }

    // Method to switch to the registration page
    private void registerActivity() {
        // Create intent to start register activity
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

    // Method to switch to the login page
    private void loginActivity() {
        // Create intent to start login activity
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
}
