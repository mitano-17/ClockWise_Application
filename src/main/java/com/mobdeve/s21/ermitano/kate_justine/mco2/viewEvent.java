package com.example.createvent;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

public class viewEvent extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewevent_layout);
        FloatingActionButton editEventBt = findViewById(R.id.editBtn);
        editEventBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewEvent.this, editEvent.class);
                startActivity(intent);
            }
        });
        FloatingActionButton backBtn = findViewById(R.id.floatingActionBack);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
    }
}
