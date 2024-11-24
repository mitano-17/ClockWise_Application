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

public class login extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize Firebase Authentication instance
        auth = FirebaseAuth.getInstance();

        // collect data
        EditText emailEt = findViewById(R.id.emailEt);
        EditText passwordEt = findViewById(R.id.passwordEt);

        // pass to dashboard
        Button loginActivity = findViewById(R.id.loginBtn);
        loginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Added logic to authenticate user before switching activity
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();

                // Validate that email and password fields are not empty
                if (email.isEmpty()) {
                    emailEt.setError("Email cannot be empty");
                    return; // Stop execution if email is empty
                }
                if (password.isEmpty()) {
                    passwordEt.setError("Password cannot be empty");
                    return; // Stop execution if password is empty
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(login.this, dashboard.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(login.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}
