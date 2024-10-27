package com.mobdeve.s21.ermitano.kate_justine.mco2;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class profile extends AppCompatActivity {
    private TextView imgLogout;
    private TextView imgReport;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgLogout = findViewById(R.id.logout);
        imgReport = findViewById(R.id.report);
        imgBack = findViewById(R.id.back);

        // Handle click events for the Logout image
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Handle click events for the Logout image
        imgReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile.this, report.class);
                startActivity(intent);
                finish();
            }
        });

        // Handle click events for the Back image
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile.this, dashboard.class);
                startActivity(intent);
                finish();
            }
        });

    }
}