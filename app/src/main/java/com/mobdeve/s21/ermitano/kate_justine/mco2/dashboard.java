package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class dashboard extends AppCompatActivity {

    private RecyclerView eventsRecycler;
    private dashboard_adapter myAdapter;
    private ArrayList<course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set to dashboard as default
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // set greeting with full name
        TextView textView = findViewById(R.id.nameGreetTv);
        String name = getIntent().getStringExtra("NameGreeting");

        textView.setText(name);

        // set layout recycler view
        eventsRecycler = findViewById(R.id.EventsRv);
        eventsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // fill course list
        courseList = placeholderData.generateCourseData();

        // fill recycler view data
        myAdapter = new dashboard_adapter(this, courseList);
        eventsRecycler.setAdapter(myAdapter);

        Button createBtn = findViewById(R.id.createEventBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, createEvent.class);
                startActivity(intent);
            }
        });

        Button joinBtn = findViewById(R.id.joinEventBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, joinEventqr.class);
                startActivity(intent);
            }
        });
    }
}
