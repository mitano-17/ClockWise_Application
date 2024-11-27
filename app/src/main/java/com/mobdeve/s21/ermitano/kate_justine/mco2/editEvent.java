package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import yuku.ambilwarna.AmbilWarnaDialog;

public class editEvent extends AppCompatActivity {

    private EditText eEventTitleTv, eStartDateTv, eStartTimeTv, eEndDateTv, eEndTimeTv, eNumAttendees;
    private ChipGroup chipGroup;
    private ImageView addIcon, colorPickerImg;
    String eventId;

    private FirebaseFirestore db;
    int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editevent_layout);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");
        String eventName = intent.getStringExtra("eventName");
        String startDate = intent.getStringExtra("startDate");
        String startTime = intent.getStringExtra("startTime");
        String endDate = intent.getStringExtra("endDate");
        String endTime = intent.getStringExtra("endTime");
        String numAtendees = intent.getStringExtra("numAtendees");

        eEventTitleTv = findViewById(R.id.EventTitleInput);
        eEventTitleTv.setText(eventName);
        eStartDateTv = findViewById(R.id.startDateTv);
        eStartDateTv.setText(startDate);
        eStartTimeTv = findViewById(R.id.startTimeTv);
        eStartTimeTv.setText(startTime);
        eEndDateTv = findViewById(R.id.endDateTv);
        eEndDateTv.setText(endDate);
        eEndTimeTv = findViewById(R.id.endTimeTv);
        eEndTimeTv.setText(endTime);
        eNumAttendees = findViewById(R.id.NumTv);
        eNumAttendees.setText(numAtendees);
        chipGroup = findViewById(R.id.chipGroup);
        addIcon = findViewById(R.id.addIcon);
        colorPickerImg = findViewById(R.id.imageView2);

        //Start Date picker
        eStartDateTv.setOnClickListener(view -> datePicker(eStartDateTv));
        //End date picker
        eEndDateTv.setOnClickListener(view -> datePicker(eEndDateTv));
        //Start time picker
        eStartTimeTv.setOnClickListener(view -> timePicker(eStartTimeTv));
        //End time picker
        eEndTimeTv.setOnClickListener(view -> timePicker(eEndTimeTv));
        //Add event type
        addIcon.setOnClickListener(view -> showEventTypeDialog(chipGroup));

        // Save event changes
        FloatingActionButton saveBt = findViewById(R.id.saveBt);
        saveBt.setOnClickListener(v -> saveChanges());

        //Delete event
        FloatingActionButton trashBt = findViewById(R.id.trashBt);
        trashBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });

        // Back to previous screen
        ImageView backBtn = findViewById(R.id.backImg2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Color scheme dialog box
        defaultColor = ContextCompat.getColor(editEvent.this, com.google.android.material.R.color.design_default_color_primary);
        colorPickerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
    }

    private void saveChanges() {

        String upEventTitle = eEventTitleTv.getText().toString().trim();
        String upStartDate = eStartDateTv.getText().toString().trim();
        String upStartTime = eStartTimeTv.getText().toString().trim();
        String upEndDate = eEndDateTv.getText().toString().trim();
        String upEndTime = eEndTimeTv.getText().toString().trim();
        String upNumAttendees = eNumAttendees.getText().toString().trim();
        String upColor = String.format("#%06X", (0xFFFFFF & defaultColor));
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String selectedOption = selectedRadioButton.getText().toString();
        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        StringBuilder tagsBuilder = new StringBuilder();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            tagsBuilder.append(chip.getText().toString()).append(" ");
        }
        String upEventTypes = tagsBuilder.toString();

        if (upEventTitle.isEmpty() || upStartDate.isEmpty() || upStartTime.isEmpty() || upEndDate.isEmpty() || upEndTime.isEmpty() || upNumAttendees.isEmpty()) {
            Toast.makeText(editEvent.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (chipGroup.getChildCount() == 0) {
            Toast.makeText(editEvent.this, "Please add at least one event type tag.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedRadioButtonId == -1) {
            Toast.makeText(editEvent.this, "Please select a receive alert option.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (upColor.equals("#FFFFFF")) {
            Toast.makeText(editEvent.this, "Please select an icon color.", Toast.LENGTH_SHORT).show();
            return;
        }

       // Event updatedEvent = new Event(upEventTitle, upStartDate, upStartTime, upEndDate, upEndTime, upNumAttendees, upColor, selectedOption, upEventTypes);

        Map<String, Object> updatedEvent = new HashMap<>();
        updatedEvent.put("eventName", upEventTitle);
        updatedEvent.put("startDate", upStartDate);
        updatedEvent.put("startTime", upStartTime);
        updatedEvent.put("endDate", upEndDate);
        updatedEvent.put("endTime", upEndTime);
        updatedEvent.put("numAttendees", upNumAttendees);
        updatedEvent.put("color", upColor);
        updatedEvent.put("eventTags", upEventTypes);

        db.collection("events").document(eventId).update(updatedEvent)
                .addOnSuccessListener(aVoid -> {
                Toast.makeText(editEvent.this, "Event Updated Successfully.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(editEvent.this, viewEvent.class);
                intent.putExtra("eventId", eventId);
                intent.putExtra("eventName", upEventTitle);
                intent.putExtra("startDate", upStartDate);
                intent.putExtra("startTime", upStartTime);
                intent.putExtra("endDate", upEndDate);
                intent.putExtra("endTime", upEndTime);
                intent.putExtra("numAttendees", upNumAttendees);
                intent.putExtra("color", upColor);
                intent.putExtra("eventTags", upEventTypes);
                setResult(RESULT_OK, intent);
                finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(editEvent.this, "Error in updating the event.", Toast.LENGTH_SHORT).show();
        });

    }

    private void deleteEvent() {
        new AlertDialog.Builder(this).setTitle("Delete Event").setMessage("Are you sure you want to delete this event?").setPositiveButton("Yes", (dialog, which) -> {
            db.collection("events").document(eventId).delete().addOnSuccessListener(aVoid -> {
                Toast.makeText(editEvent.this, "Event Deleted.", Toast.LENGTH_SHORT).show();
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(editEvent.this, "Failed to delete the event.", Toast.LENGTH_SHORT).show();
            });
        }).setNegativeButton("No", null).show();
    }

    public void openColorPicker() {
        ImageView colorPickerImg = findViewById(R.id.imageView2);
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

    private void datePicker(EditText dateInput) {

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

    private void timePicker(EditText timeInput) {
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
                    } else {
                        Toast.makeText(this, "Please enter a type", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }
}







