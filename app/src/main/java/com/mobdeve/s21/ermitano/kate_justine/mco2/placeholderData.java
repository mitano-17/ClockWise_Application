package com.mobdeve.s21.ermitano.kate_justine.mco2;

import java.util.ArrayList;
import java.util.Collections;

public class placeholderData {
    private final static course course1 = new course("GELITPH", "Monday","9:15 AM - 10:45 AM", "#ff5733");
    private final static course course2 = new course("GESPORT", "Monday","9:00 AM - 12:00 PM", "#6aff33");
    private final static course course3 = new course("CCINFOM", "Tuesday","7:30 AM - 9:00 AM", "#33d3ff");
    private final static course course4 = new course("CSMATH1", "Thursday","12:45 PM - 2:15 PM", "#ffaa33");
    private final static course course5 = new course("CSALGCM", "Thursday","2:30 PM - 4:00 PM", "#cf33ff");
    private final static course course6 = new course("GERIZAL", "Friday","9:15 AM - 10:45 AM", "#ff33df");
    private final static course course7 = new course("ORG", "Wednesday","1:00 PM - 3:00 PM", "#3379ff");

    public static ArrayList<course> generateCourseData() {
        ArrayList<course> tempList = new ArrayList<course>();

        tempList.add(course1);
        tempList.add(course2);
        tempList.add(course3);
        tempList.add(course4);
        tempList.add(course5);
        tempList.add(course6);
        tempList.add(course7);

        return tempList;
    }


}
