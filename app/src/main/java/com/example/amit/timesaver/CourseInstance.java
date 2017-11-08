package com.example.amit.timesaver;

import java.io.Serializable;

/**
 * Created by amit on 13/10/17.
 */

class CourseInstance implements Serializable{

    private static final long serialVersionUID = 1L;


    private Course course;
    private eDay dayOfWeek;
    private int startHour;
    private int endHour;
    private String professorName;
    private MyDate day;

    enum eDay {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }

    public CourseInstance() {
    }

    public CourseInstance(Course course,MyDate day, eDay dayOfWeek, int startHour, int endHour, String professorName) {
        this.course = course;
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
        this.professorName = professorName;
        this.day = day;
    }

    public CourseInstance(Course course,MyDate day, eDay dayOfWeek, int startHour, int endHour) {
        this(course,day, dayOfWeek,startHour,endHour, null);
    }

    public Course getCourseName() {
        return course;
    }

    public MyDate getDay() {
        return day;
    }

    public eDay getDayOfWeek() {
        return dayOfWeek;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public String getProfessorName() {
        return professorName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CourseInstance) {
            CourseInstance temp = (CourseInstance) obj;
            return course.equals(temp.getCourseName()) && startHour == temp.getStartHour();
        }
        return false;
    }
}
