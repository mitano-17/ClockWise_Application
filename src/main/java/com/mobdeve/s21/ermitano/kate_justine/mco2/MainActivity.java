package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView eventsRecycler;
    private dashboard_adapter myAdapter;
    private ArrayList<course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set to dashboard as default
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // set layout recycler view
        eventsRecycler = findViewById(R.id.EventsRv);
        eventsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // fill course list
        courseList = placeholderData.generateCourseData();

        // fill recycler view data
        myAdapter = new dashboard_adapter(this, courseList);
        eventsRecycler.setAdapter(myAdapter);



    }
}