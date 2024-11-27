package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Event {
    private String eventName;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String numAttendees;
    private String color;
    private String receiveAlert;
    private String eventType;

    public Event() {}

    public Event(String eventName, String startDate, String startTime, String endDate, String endTime, String numAttendees, String color, String receiveAlert, String eventType) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.numAttendees = numAttendees;
        this.color = color;
        this.receiveAlert = receiveAlert;
        this.eventType = eventType;
    }

    // Getters and setters
    public String getEventName() { return eventName; }
    public String getStartDate() { return startDate; }
    public String getStartTime() { return startTime; }
    public String getEndDate() { return endDate; }
    public String getEndTime() { return endTime; }
    public String getNumAttendees() { return numAttendees; }
    public String getColor () { return color; }
    public String getReceiveAlert() { return receiveAlert; }
    public String getEventType() { return eventType; }
}
