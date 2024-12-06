package com.mobdeve.s21.ermitano.kate_justine.mco2;

import java.util.ArrayList;

public class attendeeForm {
    private String userId;
    private String eventId;
    private ArrayList<String> fields;
    private String formId;

    public attendeeForm() {}

    public attendeeForm(String userId, String eventId, ArrayList<String> fields, String formId) {
        this.userId = userId;
        this.eventId = eventId;
        this.fields = fields;
        this.formId = formId;
    }

    public String getUserId() {
        return userId;
    }

    public String getEventId() {
        return eventId;
    }

    public ArrayList<String> getFields() {
        return fields;
    }
    public String getFormId() {
        return formId;
    }

}
