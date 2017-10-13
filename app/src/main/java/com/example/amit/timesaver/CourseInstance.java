package com.example.amit.timesaver;

/**
 * Created by amit on 13/10/17.
 */

class CourseInstance {

    private Course course;
    private int[] weeklyDays;
    private int[] weeklyHours;
    private String professorName;

    public CourseInstance(Course course, int[] weeklyDays, int[] weeklyHours, String professorName) {
        this.course = course;
        this.weeklyDays = weeklyDays;
        this.weeklyHours = weeklyHours;
        this.professorName = professorName;
    }

    public CourseInstance(Course course, int[] weeklyDays, int[] weeklyHours) {
        this(course, weeklyDays, weeklyHours, null);
    }

    public Course getCourse() {
        return course;
    }

    public int[] getWeeklyDays() {
        return weeklyDays;
    }

    public int[] getWeeklyHours() {
        return weeklyHours;
    }

    public String getProfessorName() {
        return professorName;
    }
}
