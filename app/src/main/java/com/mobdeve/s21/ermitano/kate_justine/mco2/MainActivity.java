package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set to welcome as default
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);



        // register button
        Button registerActivity = findViewById(R.id.SignUpBtn);
        registerActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerActivity();
            }
        });

        // login button
        Button loginActivity = findViewById(R.id.loginBtn);
        loginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginActivity();
            }
        });
    }

    // switch to registration page
    private void registerActivity() {
        // create intent
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

    // switch to login page
    private void loginActivity() {
        // create intent
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
}
