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

    public void addCourse(String courseKey, Course course) {
        courses.put(courseKey, course);
    }

    public void removeCourse(String courseKey) {
        courses.remove(courseKey);
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

    public ArrayList<Course> getArrayListCourses() {
        ArrayList<Course> returnCourses = new ArrayList<>(courses.size());
        for (Course c: courses.values()) {
            returnCourses.add(c);
        }
        return returnCourses;
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
        return courses.size();
    }

    public eSemesterType getSemesterType() {
        return semesterType;
    }

    @Override
    public boolean equals(Object semester) {
        return name.equals(((Semester)semester).getName());
    }



}
