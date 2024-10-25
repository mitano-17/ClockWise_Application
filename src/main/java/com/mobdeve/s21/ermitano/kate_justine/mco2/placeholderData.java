package com.mobdeve.s21.ermitano.kate_justine.mco2;

import java.util.ArrayList;
import java.util.Collections;

public class placeholderData {
    private final static course course1 = new course("GELITPH", "Monday","9:15 AM - 10:45 AM");
    private final static course course2 = new course("GESPORT", "Monday","9:15 AM - 10:45 AM");
    private final static course course3 = new course("CCINFOM", "Monday","9:15 AM - 10:45 AM");
    private final static course course4 = new course("CSMATH1", "Monday","9:15 AM - 10:45 AM");
    private final static course course5 = new course("CSALGCM", "Monday","9:15 AM - 10:45 AM");
    private final static course course6 = new course("GERIZAL", "Monday","9:15 AM - 10:45 AM");
    private final static course course7 = new course("ORG", "Monday","9:15 AM - 10:45 AM");

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
