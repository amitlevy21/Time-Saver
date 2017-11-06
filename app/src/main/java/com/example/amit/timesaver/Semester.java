package com.example.amit.timesaver;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by amit on 13/10/17.
 */


public class Semester implements Serializable{


    enum eSemesterType  {A, B, C}

    private static final long serialVersionUID = 1L;
    private int year;
    private MyDate startDate;
    private MyDate endDate;
    private int numOfCourses;
    private HashMap<String,Course> courses;
    private eSemesterType semesterType;
    private String name;

    public Semester() {
        courses = new HashMap<>();
    }

    public Semester(int year, MyDate startDate, MyDate endDate, eSemesterType semesterType) {
        this.year = year;
        this.startDate = startDate;
        this.endDate = endDate;
        this.semesterType = semesterType;
        name = year +" - " + semesterType.name();
        courses = new HashMap<>();
    }

    public void addCourse(String semesterKey, Course course) {
        courses.put(semesterKey, course);
        numOfCourses++;
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        if(numOfCourses != 0)
            numOfCourses--;
    }

    public Course getCourseByName (String courseName) {
        for (Course course : courses.values()) {
            if (course.getName().equals(courseName))
                return course;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Course> getCourses() {
        return courses;
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

    public eSemesterType getSemesterType() {
        return semesterType;
    }

    @Override
    public boolean equals(Object semester) {
        return name.equals(((Semester)semester).getName());
    }



}
