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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinevent_layout2);  // Ensure this layout has the TextView with ID eventName

        ImageView backBt = findViewById(R.id.backImg4);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Retrieve the event name from the Intent
        TextView eventName = findViewById(R.id.eventName);  // Make sure eventName ID is correct

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("eventName")) {
            String eventNameV = intent.getStringExtra("eventName");  // Get the event name
            eventName.setText(eventNameV);  // Set the event name to the TextView
        }
    }
}

