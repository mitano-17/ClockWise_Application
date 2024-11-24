package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {

    private String fullName;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        auth = FirebaseAuth.getInstance();
        // pass to dashboard
        Button signUpActivity = findViewById(R.id.signUpBtn);
        signUpActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                signUpActivity();
            }
        });

    }

    // switch to dashboard page
    private void signUpActivity() {
        // collect data
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
            return;  // Stop further execution if fields are empty
        }

        if (!password.equals(reEnterPassword)) {
            Toast.makeText(register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;  // Stop if passwords do not match
        }

        // Create a new user in Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign-up successful, proceed to the dashboard
                            Toast.makeText(register.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(register.this, dashboard.class);
                            intent.putExtra("NameGreeting", fullName);
                            startActivity(intent);
                        } else {
                            // Sign-up failed, display the error message
                            Toast.makeText(register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
