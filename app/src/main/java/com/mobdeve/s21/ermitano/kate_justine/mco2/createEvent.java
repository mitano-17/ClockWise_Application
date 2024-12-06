package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Date;
import java.util.Set;

import yuku.ambilwarna.AmbilWarnaDialog;


public class createEvent extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createevent_layout);
        EdgeToEdge.enable(this);

        //Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //Initialize UI elements
        EditText cEventTitleTv = findViewById(R.id.EventTitleInput);
        EditText cStartDateTv = findViewById(R.id.startDateTv);
        EditText cStartTimeTv = findViewById(R.id.startTimeTv);
        EditText cEndDateTv= findViewById(R.id.endDateTv);
        EditText cEndTimeTv= findViewById(R.id.endTimeTv);
        EditText cNumTv = findViewById(R.id.NumTv);
        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        ImageView addIcon = findViewById(R.id.addIcon);
        ImageView colorPickerImg = findViewById(R.id.colorPicker);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        //click listeners for the elements
        FloatingActionButton createEventBt = findViewById(R.id.createEvntBt);
        cStartDateTv.setOnClickListener(view -> datePicker(cStartDateTv));
        cEndDateTv.setOnClickListener(view -> datePicker(cEndDateTv));
        cStartTimeTv.setOnClickListener(view -> timePicker(cStartTimeTv));
        cEndTimeTv.setOnClickListener(view -> timePicker(cEndTimeTv));
        addIcon.setOnClickListener(view -> showEventTypeDialog(chipGroup));


       Button setLocBt = findViewById(R.id.setLocationBt);

        //click listener for creating the event
        createEventBt.setOnClickListener(view -> {

            //gathers user input
            String userId = auth.getCurrentUser().getUid();
            String cEventName = cEventTitleTv.getText().toString().trim();
            String cStartDate = cStartDateTv.getText().toString().trim();
            String cStartTime = cStartTimeTv.getText().toString().trim();
            String cEndDate = cEndDateTv.getText().toString().trim();
            String cEndTime = cEndTimeTv.getText().toString().trim();
            String cNum = cNumTv.getText().toString().trim();
            String cColor = String.format("#%06X", (0xFFFFFF & defaultColor));

            //retrieve selected radio button text
            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedOption = selectedRadioButton.getText().toString();

            //gather event tags
            StringBuilder tagsBuilder = new StringBuilder();
            Set<String> eventTags = new HashSet<>();
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                eventTags.add(chip.getText().toString().trim());
            }
            for(String event: eventTags){
                tagsBuilder.append(event).append(" ");
            }
            String cEventTypes = tagsBuilder.toString().trim();

            //validate inputs
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

            if (!validFormat(cStartDate, cStartTime, cEndDate, cEndTime)) {
                return;
            }
            //validate date and time input
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

            try {
                Date startDate = dateFormat.parse(cStartDate);
                Date endDate = dateFormat.parse(cEndDate);
                Date startTime = timeFormat.parse(cStartTime);
                Date endTime = timeFormat.parse(cEndTime);

                if (endDate.before(startDate)) {
                    cEndDateTv.setError("Event End Date must not be earlier than Start Date.");
                    return;
                }
                if (startDate.equals(endDate)) {
                    if (endTime.before(startTime)) {
                        cEndTimeTv.setError("Event End Time must not be earlier than Start Time");
                        return;
                    }
                }
            }catch (ParseException e){
                Toast.makeText(this, "Invalid Date", Toast.LENGTH_SHORT).show();
                return;
            }

            if(cNum.isEmpty()){
                cNumTv.setError("Please indicate the number of attendees.");
                return;
            }

            int numAttendees = Integer.parseInt(cNum);
            if(numAttendees < 2 ){
                cNumTv.setError("Number of attendees must be greater than 1");
                return;
            }
            if(numAttendees > 200){
                cNumTv.setError("Number of attendees is limited to 200 attendees only.");
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

            //Create event object
            String eventTypes = cEventTypes;

            Event event = new Event(userId, null, cEventName, cStartDate, cStartTime, cEndDate, cEndTime, cNum, cColor, selectedOption, cEventTypes);
            db.collection("users").document(userId).collection("events")
                    .add(event).addOnSuccessListener(documentReference -> {
                        String eventId = documentReference.getId();
                        Toast.makeText(createEvent.this, "Event created successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(createEvent.this, viewEvent.class);
                        intent.putExtra("userId", userId);
                        intent.putExtra("eventId", eventId);
                        intent.putExtra("eventName", cEventName);
                        intent.putExtra("startDate", cStartDate);
                        intent.putExtra("startTime", cStartTime);
                        intent.putExtra("endDate", cEndDate);
                        intent.putExtra("endTime", cEndTime);
                        intent.putExtra("numAttendees", cNum);
                        intent.putExtra("color", cColor);
                        intent.putExtra("receiveAlert", selectedOption);
                        intent.putExtra("eventType", eventTypes);

                        startActivity(intent);

                    }).addOnFailureListener(e -> {
                        Toast.makeText(createEvent.this, "Error in creating event.", Toast.LENGTH_SHORT).show();
                    });
            //if device is offline, show a message
            if(!isOnline()){
                new AlertDialog.Builder(this).setTitle("Offline").setMessage("Your event has been created. You can view and edit your event once you are back online.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            finish();
                        })
                        .show();
            }

        });


        //click listener for back button
        ImageView BackBt = findViewById(R.id.backImgVw);
        BackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //default color and click listener for the color picker dialog box
        defaultColor = ContextCompat.getColor(createEvent.this, com.google.android.material.R.color.design_default_color_primary);
        colorPickerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });


    }

    //opens the color picker dialog box
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

    //opens the date picker dialog box
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

    //opens the time picker dialog box
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

    //opens the event type dialog box
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
                    }
                    else {
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

    private boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}

