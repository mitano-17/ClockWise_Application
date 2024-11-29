package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class enterEventqr extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinevent_layout2);

        ImageView backBt = findViewById(R.id.backImg4);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView eventName = findViewById(R.id.eventName);
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("scannedData")){
            String eventNameV = intent.getStringExtra("scannedData");
            eventName.setText(eventNameV);
        }
}}
