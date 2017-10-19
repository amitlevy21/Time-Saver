package com.example.amit.timesaver;

import java.util.ArrayList;

/**
 * Created by amit on 13/10/17.
 */


public class Semester {

    enum eSemesterType  {A, B, C}

    private int year;
    private MyDate startDate;
    private MyDate endDate;
    private int numOfCourses;
    private ArrayList<Course> courses = new ArrayList<>();
    private eSemesterType semesterTypeArr;

    public Semester(int year, MyDate startDate, MyDate endDate, eSemesterType semesterTypeArr) {
        this.year = year;
        this.startDate = startDate;
        this.endDate = endDate;
        this.semesterTypeArr = semesterTypeArr;
    }

    public void addCourse(Course course) {
        courses.add(course);
        numOfCourses++;
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        if(numOfCourses != 0)
            numOfCourses--;
    }

    public int getYear() {
        return year;
    }

    public MyDate getStartDate() {
        return startDate;
    }

    public MyDate getEndDate() {
        return endDate;
    }

    public int getNumOfCourses() {
        return numOfCourses;
    }

    public Course getCourseAtIndex(int index) {
        return courses.get(index);
    }
}
