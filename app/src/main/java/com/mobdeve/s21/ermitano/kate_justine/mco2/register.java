package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance(); // Initialize Firestore

        // Sign-Up button functionality
        Button signUpActivity = findViewById(R.id.signUpBtn);
        signUpActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpActivity();
            }
        });
    }

    private void signUpActivity() {
        // Collect data from input fields
        EditText fullNameEt = findViewById(R.id.fullNameEt);
        EditText companyEt = findViewById(R.id.companyEt);
        EditText emailEt = findViewById(R.id.emailEt);
        EditText passwordEt = findViewById(R.id.passwordEt);
        EditText reEnterPasswordEt = findViewById(R.id.reEnterPasswordEt);

        String fullName = fullNameEt.getText().toString();
        String company = companyEt.getText().toString();
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        String reEnterPassword = reEnterPasswordEt.getText().toString().trim();

        // Validate inputs
        if (fullName.isEmpty() || company.isEmpty() || email.isEmpty() || password.isEmpty() || reEnterPassword.isEmpty()) {
            Toast.makeText(register.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(reEnterPassword)) {
            Toast.makeText(register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user in Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Get the UID of the newly registered user
                            String userId = auth.getCurrentUser().getUid();

                            // Prepare user data to save to Firestore
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("fullName", fullName);
                            userData.put("company", company);
                            userData.put("email", email);

                            // Save data to Firestore
                            firestore.collection("users").document(userId).set(userData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> firestoreTask) {
                                            if (firestoreTask.isSuccessful()) {
                                                // Navigate to the dashboard after successful Firestore save
                                                Toast.makeText(register.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(register.this, dashboard.class);
                                                intent.putExtra("NameGreeting", fullName);
                                                startActivity(intent);
                                            } else {
                                                // Firestore save failed
                                                Toast.makeText(register.this, "Failed to save user data: " + firestoreTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Firebase Authentication failed
                            Toast.makeText(register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
