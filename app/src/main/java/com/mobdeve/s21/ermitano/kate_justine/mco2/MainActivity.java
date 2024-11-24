package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
