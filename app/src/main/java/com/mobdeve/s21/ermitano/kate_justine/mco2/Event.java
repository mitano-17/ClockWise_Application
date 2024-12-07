package com.mobdeve.s21.ermitano.kate_justine.mco2;

import com.google.firebase.firestore.IgnoreExtraProperties;
import java.util.List;

@IgnoreExtraProperties
public class Event {
    private String userId;
    private String eventId;
    private String eventName;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String numAttendees;
    private String color;
    private String receiveAlert;
    private String eventType;
    //private List<String> attendeeFormFields;

    public Event() {}

    public Event(String userId, String eventId, String eventName, String startDate, String startTime, String endDate, String endTime, String numAttendees, String color, String receiveAlert, String eventType,  String qrCodeData) {
        this.userId = userId;
        this.eventId = eventId;
        this.eventName = eventName;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.numAttendees = numAttendees;
        this.color = color;
        this.receiveAlert = receiveAlert;
        this.eventType = eventType;
        //this.attendeeFormFields = attendeeFormFields;
    }

    public String getUserId(){ return userId; }
    public String getEventId(){ return eventId; }
    public String getEventName() { return eventName; }
    public String getStartDate() { return startDate; }
    public String getStartTime() { return startTime; }
    public String getEndDate() { return endDate; }
    public String getEndTime() { return endTime; }
    public String getNumAttendees() { return numAttendees; }
    public String getColor () { return color; }
    public String getReceiveAlert() { return receiveAlert; }
    public String getEventType() { return eventType; }
    //public List<String> getAttendeeFormFields() { return attendeeFormFields; }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
