package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class register extends AppCompatActivity {

    private String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // pass to dashboard
        Button signUpActivity = findViewById(R.id.signUpBtn);
        signUpActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpActivity();
            }
        });

    }

    // switch to dashboard page
    private void signUpActivity() {
        // collect data
        EditText fullNameEt = findViewById(R.id.fullNameEt);
        EditText companyEt = findViewById(R.id.companyEt);
        EditText emailTv = findViewById(R.id.emailEt);
        EditText passwordEt = findViewById(R.id.passwordEt);
        EditText reEnterPasswordEt = findViewById(R.id.reEnterPasswordEt);

        String fullName = fullNameEt.getText().toString();
        Intent intent = new Intent(this, dashboard.class);
        intent.putExtra("NameGreeting",  fullName);
        startActivity(intent);
    }
}
