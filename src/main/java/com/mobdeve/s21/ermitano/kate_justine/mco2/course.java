package com.mobdeve.s21.ermitano.kate_justine.mco2;

public class course {
    private String courseTitle;
    private String courseDay;
    private String courseTime;

    public course(String courseTitle, String courseDay, String courseTime) {
        this.courseTitle = courseTitle;
        this.courseDay = courseDay;
        this.courseTime = courseTime;
    }

    public String getCourseTitle() {
        return this.courseTitle;
    }

    public String getCourseDay() {
        return this.courseDay;
    }

    public String getCourseTime() {
        return this.courseTime;
    }
}
