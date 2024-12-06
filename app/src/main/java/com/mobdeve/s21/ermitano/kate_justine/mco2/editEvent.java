package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import yuku.ambilwarna.AmbilWarnaDialog;

public class editEvent extends AppCompatActivity {

    private EditText eEventTitleTv, eStartDateTv, eStartTimeTv, eEndDateTv, eEndTimeTv, eNumAttendees, eColor, eReceiveAlert, eEventType;
    private ChipGroup chipGroup;
    private ImageView addIcon, colorPickerImg;
    private String userId, eventId, eventName, eventType, startDate, startTime, endDate, endTime, numAttendees, color, receiveAlert;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private RadioButton bt1, bt2,bt3;
    int selectedRadioButtonId;
    int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editevent_layout);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        eventId = intent.getStringExtra("eventId");
        eventName = intent.getStringExtra("eventName");
        startDate = intent.getStringExtra("startDate");
        startTime = intent.getStringExtra("startTime");
        endDate = intent.getStringExtra("endDate");
        endTime = intent.getStringExtra("endTime");
        numAttendees = intent.getStringExtra("numAttendees");
        eventType = intent.getStringExtra("eventType");
        receiveAlert = intent.getStringExtra("receiveAlert");
        color = intent.getStringExtra("color");

        eEventTitleTv = findViewById(R.id.EventTitleInput);
        eStartDateTv = findViewById(R.id.startDateTv);
        eStartTimeTv = findViewById(R.id.startTimeTv);
        eEndDateTv = findViewById(R.id.endDateTv);
        eEndTimeTv = findViewById(R.id.endTimeTv);
        eNumAttendees = findViewById(R.id.NumTv);
        chipGroup = findViewById(R.id.chipGroup);
        addIcon = findViewById(R.id.addIcon);
        colorPickerImg = findViewById(R.id.imageView2);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        bt1 = findViewById(R.id.radioButton);
        bt2 = findViewById(R.id.radioButton2);
        bt3 = findViewById(R.id.radioButton3);
        selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

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

        loadEvent();

        // Save event changes
        FloatingActionButton saveBt = findViewById(R.id.saveBt);
        saveBt.setOnClickListener(v -> saveChanges());

        //Delete event
        FloatingActionButton trashBt = findViewById(R.id.trashBt);
        trashBt.setOnClickListener(v -> deleteEvent());

        // Back to previous screen
        ImageView backBtn = findViewById(R.id.backImg2);
        backBtn.setOnClickListener(v -> finish());

        //Color scheme dialog box
        defaultColor = ContextCompat.getColor(editEvent.this, com.google.android.material.R.color.design_default_color_primary);
        colorPickerImg.setOnClickListener(v -> openColorPicker());

    }

    //loads initial details
    private void loadEvent(){
        String userId = auth.getCurrentUser().getUid();

        db.collection("users").document(userId).collection("events").document(eventId)
                .get().addOnSuccessListener(document -> {
                    if(document.exists()){
                        eEventTitleTv.setText(eventName);
                        eStartDateTv.setText(startDate);
                        eStartTimeTv.setText(startTime);
                        eEndDateTv.setText(endDate);
                        eEndTimeTv.setText(endTime);
                        eNumAttendees.setText(numAttendees);
                        receiveAlert =document.getString("receiveAlert");
                        color = document.getString("color");
                        if (color != null) {
                            defaultColor = Color.parseColor(color);
                            colorPickerImg.setBackgroundColor(defaultColor);
                        }

                        if (eventType != null && !eventType.isEmpty()) {
                            String[] tags = eventType.split(" ");
                            for (String tag : tags) {
                                Chip chip = new Chip(this);
                                chip.setText(tag);
                                chip.setCloseIconVisible(true);
                                chip.setOnCloseIconClickListener(v -> chipGroup.removeView(chip));
                                chipGroup.addView(chip);
                            }
                        }
                        if ("1 hour before event".equals(receiveAlert)) {
                            bt1.setChecked(true);
                        } else if ("1 day before event".equals(receiveAlert)) {
                            bt2.setChecked(true);
                        } else if ("Never".equals(receiveAlert)) {
                            bt3.setChecked(true);
                        }
                    }else{
                        Toast.makeText(this, "Event data not found.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(this, "Failed to load event", Toast.LENGTH_SHORT).show());
    }

    //method to save the updates and input validations
    private void saveChanges() {
        String upEventTitle = eEventTitleTv.getText().toString();
        String upStartDate = eStartDateTv.getText().toString();
        String upStartTime = eStartTimeTv.getText().toString();
        String upEndDate = eEndDateTv.getText().toString();
        String upEndTime = eEndTimeTv.getText().toString();
        String upNumAttendees = eNumAttendees.getText().toString();
        String upColor = String.format("#%06X", (0xFFFFFF & defaultColor));

        //For collecting event types
        StringBuilder tagsBuilder = new StringBuilder();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            tagsBuilder.append(chip.getText().toString()).append(" ");
        }
        String upEventTypes = tagsBuilder.toString().trim();

        // Input validation
        if (upEventTitle.isEmpty() || upStartDate.isEmpty() || upStartTime.isEmpty() ||
                upEndDate.isEmpty() || upEndTime.isEmpty() || upNumAttendees.isEmpty()) {
            Toast.makeText(editEvent.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validFormat(upStartDate, upStartTime, upEndDate, upEndTime)) {
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        try {
            Date startDate = dateFormat.parse(upStartDate);
            Date endDate = dateFormat.parse(upEndDate);
            Date startTime = timeFormat.parse(upStartTime);
            Date endTime = timeFormat.parse(upEndTime);

            if (endDate.before(startDate)) {
                eEndDateTv.setError("Event End Date must not be earlier than Start Date.");
                return;
            }
            if (startDate.equals(endDate)) {
                if (endTime.before(startTime)) {
                    eEndTimeTv.setError("Event End Time must not be earlier than Start Time");
                    return;
                }
            }
        }catch (ParseException e){
            Toast.makeText(this, "Invalid Date", Toast.LENGTH_SHORT).show();
            return;
        }

        int numAttendees = Integer.parseInt(upNumAttendees);
        if(numAttendees < 2 ){
            eNumAttendees.setError("Number of attendees must be greater than 1");
            return;
        }
        if(numAttendees > 200){
            eNumAttendees.setError("Number of attendees is limited to 200 attendees only.");
            return;
        }

        if (chipGroup.getChildCount() == 0) {
            Toast.makeText(editEvent.this, "Please add at least one event type tag.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userId == null || userId.isEmpty() || eventId == null || eventId.isEmpty()) {
            Toast.makeText(this, "Event ID or User ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedRadioButtonId == -1) {
            Toast.makeText(editEvent.this, "Please select only one alert option.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare updated event data
        String userId = auth.getCurrentUser().getUid();
        Map<String, Object> updatedEvent = new HashMap<>();
        updatedEvent.put("eventName", upEventTitle);
        updatedEvent.put("startDate", upStartDate);
        updatedEvent.put("startTime", upStartTime);
        updatedEvent.put("endDate", upEndDate);
        updatedEvent.put("endTime", upEndTime);
        updatedEvent.put("numAttendees", upNumAttendees);
        updatedEvent.put("color", upColor);
        updatedEvent.put("eventType", upEventTypes);

        // Update Firestore
        db.collection("users").document(userId).collection("events").document(eventId)
                .update(updatedEvent)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event Updated Successfully.", Toast.LENGTH_SHORT).show();
                    Intent result = new Intent();
                    result.putExtra("eventName", upEventTitle);
                    result.putExtra("startDate", upStartDate);
                    result.putExtra("startTime", upStartTime);
                    result.putExtra("endDate", upEndDate);
                    result.putExtra("numAttendees", upNumAttendees);
                    result.putExtra("endTime", upEndTime);
                    result.putExtra("color", upColor);
                    result.putExtra("eventType", upEventTypes);

                    setResult(RESULT_OK, result);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(editEvent.this, "Failed to update event.", Toast.LENGTH_LONG).show());

    }

// method to delete the event
    private void deleteEvent() {
        new AlertDialog.Builder(this).setTitle("Delete Event").setMessage("Are you sure you want to delete this event?").setPositiveButton("Yes", (dialog, which) -> {
            db.collection("users").document(userId).collection("events").document(eventId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(editEvent.this, "Event Deleted.", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(editEvent.this, "Failed to delete the event.", Toast.LENGTH_SHORT).show();
                    });
        }).setNegativeButton("No", null).show();
    }

    // opens the color picker dialog
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

    //opens the date picker dialog
    private void datePicker(EditText dateInput){

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, ((view, year1, month1, dayOfMonth) -> {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            calendar.set(year1, month1, dayOfMonth);

            String formatDate = dateFormat.format(calendar.getTime());
            dateInput.setText(formatDate);
        }), year, month, day);

        datePickerDialog.show();
    }

    //opens the time picker dialog
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

    //opens the event type dialog
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

    private boolean validFormat(String startDate, String startTime, String endDate, String endTime){
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

        timeFormat.setLenient(false);
        dateFormat.setLenient(false);

        try {

            timeFormat.parse(startTime);
            timeFormat.parse(endTime);
            dateFormat.parse(startDate);
            dateFormat.parse(endDate);

            return true;
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date or time format. Please double click the space to select.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}







