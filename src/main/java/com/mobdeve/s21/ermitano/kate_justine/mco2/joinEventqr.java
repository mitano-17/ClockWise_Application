package com.example.createvent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class joinEventqr extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinevent_layout1);

        ImageView qrImg = findViewById(R.id.qr_code_image);
        qrImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(joinEventqr.this, enterEventqr.class);
                startActivity(intent);
            }
        });

        ImageView backBt = findViewById(R.id.backImg3);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

}}
