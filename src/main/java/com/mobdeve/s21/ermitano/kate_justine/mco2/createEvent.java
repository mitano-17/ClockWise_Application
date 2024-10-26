package com.example.createvent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class createEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.createevent_layout);

        FloatingActionButton createEventBt = findViewById(R.id.createEvntBt);
        createEventBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(createEvent.this, viewEvent.class);
                startActivity(intent);
            }
        });

        Button scanQR = findViewById(R.id.genQRBt); //for now
        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(createEvent.this, joinEventqr.class);
                startActivity(intent);
            }
        });

        ImageView BackBt = findViewById(R.id.backImgVw);
        BackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
}