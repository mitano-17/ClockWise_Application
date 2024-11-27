package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Calendar;

import yuku.ambilwarna.AmbilWarnaDialog;


public class createEvent extends AppCompatActivity {

    private FirebaseFirestore db;
    int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createevent_layout);

        EdgeToEdge.enable(this);
        EditText cEventTitleTv = findViewById(R.id.EventTitleInput);
        EditText cStartDateTv = findViewById(R.id.startDateTv);
        EditText cStartTimeTv = findViewById(R.id.startTimeTv);
        EditText cEndDateTv= findViewById(R.id.endDateTv);
        EditText cEndTimeTv= findViewById(R.id.endTimeTv);
        EditText cNumTv = findViewById(R.id.NumTv);
        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        ImageView addIcon = findViewById(R.id.addIcon);
        ImageView colorPickerImg = findViewById(R.id.colorPicker);
        cStartDateTv.setOnClickListener(view -> datePicker(cStartDateTv));
        cEndDateTv.setOnClickListener(view -> datePicker(cEndDateTv));
        cStartTimeTv.setOnClickListener(view -> timePicker(cStartTimeTv));
        cEndTimeTv.setOnClickListener(view -> timePicker(cEndTimeTv));
        addIcon.setOnClickListener(view -> showEventTypeDialog(chipGroup));
        FloatingActionButton createEventBt = findViewById(R.id.createEvntBt);
        createEventBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db = FirebaseFirestore.getInstance();
                String cEventName = cEventTitleTv.getText().toString().trim();
                String cStartDate = cStartDateTv.getText().toString().trim();
                String cStartTime = cStartTimeTv.getText().toString().trim();
                String cEndDate = cEndDateTv.getText().toString().trim();
                String cEndTime = cEndTimeTv.getText().toString().trim();
                String cNum = cNumTv.getText().toString().trim();
                String cColor = String.format("#%06X", (0xFFFFFF & defaultColor));
                RadioGroup radioGroup = findViewById(R.id.radioGroup);
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                ChipGroup chipGroup = findViewById(R.id.chipGroup);
                StringBuilder tagsBuilder = new StringBuilder();
                for (int i = 0; i < chipGroup.getChildCount(); i++) {
                    Chip chip = (Chip) chipGroup.getChildAt(i);
                    tagsBuilder.append(chip.getText().toString()).append(" ");
                }
                String cEventTypes = tagsBuilder.toString();

                if (cEventTypes.endsWith(" ")) {
                    cEventTypes= cEventTypes.substring(0, cEventTypes.length() - 1);
                }

                if(cEventName.isEmpty()){
                    cEventTitleTv.setError("Event Title cannot be empty.");
                    return;
                }
                if(cStartDate.isEmpty()){
                    cStartDateTv.setError("Event Start Date cannot be empty.");
                    return;
                }
                if(cStartTime.isEmpty()){
                    cStartTimeTv.setError("Event Start Time cannot be empty");
                    return;
                }
                if(cEndDate.isEmpty()){
                    cEndDateTv.setError("Event End Date cannot be empty");
                    return;
                }
                if(cEndTime.isEmpty()){
                    cEndTimeTv.setError("Event End Time cannot be empty");
                    return;
                }
                if(cNum.isEmpty()){
                    cNumTv.setError("Please indicate the number of attendees.");
                    return;
                }
                if (chipGroup.getChildCount() == 0) {
                    Toast.makeText(createEvent.this, "Please add at least one event type tag.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedRadioButtonId == -1) {
                    Toast.makeText(createEvent.this, "Please select a receive alert option.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cColor.equals("#FFFFFF")){
                    Toast.makeText(createEvent.this, "Please select an icon color.", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                String selectedOption = selectedRadioButton.getText().toString();

                Event event = new Event(cEventName, cStartDate, cStartTime, cEndDate, cEndTime, cNum, cColor, selectedOption, cEventTypes);

                String eventTypes = cEventTypes;
                db.collection("events")
                        .add(event).addOnSuccessListener(documentReference -> {
                            String eventId = documentReference.getId();
                            Toast.makeText(createEvent.this, "Event created successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(createEvent.this, viewEvent.class);

                            intent.putExtra("eventName", cEventName);
                            intent.putExtra("startDate", cStartDate);
                            intent.putExtra("startTime", cStartTime);
                            intent.putExtra("endDate", cEndDate);
                            intent.putExtra("endTime", cEndTime);
                            intent.putExtra("numAtendees", cNum);
                            intent.putExtra("color", cColor);
                            intent.putExtra("receiveAlert", selectedOption);
                            intent.putExtra("eventTags", eventTypes);

                            startActivity(intent);

                        }).addOnFailureListener(e -> {
                            Toast.makeText(createEvent.this, "Error in creating event.", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        Button generateQR = findViewById(R.id.genQRBt);
        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(createEvent.this, generatedQRcode.class);
                startActivity(intent);;
            }
        });

        ImageView BackBt = findViewById(R.id.backImgVw);
        BackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        defaultColor = ContextCompat.getColor(createEvent.this, com.google.android.material.R.color.design_default_color_primary);
        colorPickerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

    }

    public void openColorPicker(){
        ImageView colorPickerImg = findViewById(R.id.colorPicker);
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                colorPickerImg.setImageDrawable(null);
                colorPickerImg.setBackgroundColor(color);
            }
        });
        ambilWarnaDialog.show();
    }

    private void datePicker(EditText dateInput){

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, ((view, year1, month1, dayOfMonth) -> {

            String formatDate = (month1 + 1) + "/" + dayOfMonth + "/" + year1;
            dateInput.setText(formatDate);
        }), year, month, day);

        datePickerDialog.show();
    }

    private void timePicker (EditText timeInput){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String period = (hourOfDay < 12) ? "AM" : "PM";
            int hour12 = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
            if (hour12 == 0) hour12 = 12;
            String formatTime = String.format("%02d:%02d %s", hour12, minute1, period);
            timeInput.setText(formatTime);
        }, hour, minute, false);

        timePickerDialog.show();
    }

    private void showEventTypeDialog(ChipGroup chipGroup) {
        final EditText input = new EditText(this);
        input.setHint("Enter Event Type");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Event Type").setView(input).setPositiveButton("Add", (dialog, which) -> {
                    String newTag = input.getText().toString().trim();
                    if (!newTag.isEmpty()) {

                        Chip newChip = new Chip(this);
                        newChip.setText(newTag);
                        newChip.setCloseIconVisible(true);
                        newChip.setOnCloseIconClickListener(v -> {
                            chipGroup.removeView(newChip);
                        });
                        chipGroup.addView(newChip);
                    }else {
                        Toast.makeText(this, "Please enter a type", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }
}
