package com.example.amit.timesaver;

import java.io.Serializable;

/**
 * Created by amit on 13/10/17.
 */

class CourseInstance implements Serializable{

    private static final long serialVersionUID = 1L;


    private Course course;
    private int dayOfWeek;
    private int startHour;
    private int endHour;
    private String professorName;

    public CourseInstance() {
    }

    public CourseInstance(Course course, int dayOfWeek, int startHour, int endHour, String professorName) {
        this.course = course;
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
        this.professorName = professorName;
    }

    public CourseInstance(Course course, int dayOfWeek, int startHour, int endHour) {
        this(course,dayOfWeek,startHour,endHour, null);
    }

    public Course getCourse() {
        return course;
    }

    public int getDayOfWeek() {
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


}
