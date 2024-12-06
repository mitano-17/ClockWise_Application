package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

        requestLocationPermissions();

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

        if (!isOnline()) {
            Toast.makeText(this, "Please connect to the internet to register.", Toast.LENGTH_SHORT).show();
        } else {
            // Create intent to start register activity
            Intent intent = new Intent(this, register.class);
            startActivity(intent);
        }
    }

    // Method to switch to the login page
    private void loginActivity() {
        // Create intent to start login activity
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    private boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm != null){
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    private void requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Check if rationale should be shown
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                // Show a custom rationale dialog or a Toast
                showPermissionRationale();
            } else {
                // Directly request permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        100);
            }
        }
    }

    private void showPermissionRationale() {
        // Show a dialog to explain why the app needs the permissions
        new AlertDialog.Builder(this)
                .setTitle("Location Permissions Required")
                .setMessage("This app needs location permissions to provide geolocation-based services. Please grant these permissions.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Request the permissions after explaining
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            100);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle when the user denies permission
                    Toast.makeText(MainActivity.this, "Location permissions are necessary for this feature.", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
                Toast.makeText(this, "Location permissions granted.", Toast.LENGTH_SHORT).show();
            } else {
                // Permissions denied
                Toast.makeText(this, "Location permissions denied. Some features may not work.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
