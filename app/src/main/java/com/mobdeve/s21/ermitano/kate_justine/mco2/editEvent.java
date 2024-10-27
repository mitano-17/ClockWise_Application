package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

public class editEvent extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editevent_layout);

        FloatingActionButton saveBt = findViewById(R.id.saveBt);
        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(editEvent.this, viewEvent.class);
                startActivity(intent);
            }
        });

        FloatingActionButton trashBt = findViewById(R.id.trashBt);
        trashBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox();
            }
        });

        ImageView backBtn = findViewById(R.id.backImg2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
        private void dialogBox() {
            new AlertDialog.Builder(this).setTitle("Delete Event").setMessage("Are you sure you want to delete this event?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(editEvent.this, "Event Deleted.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).setNegativeButton("No", null).show();
        }

}
