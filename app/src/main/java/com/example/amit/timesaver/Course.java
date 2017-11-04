package com.example.amit.timesaver;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by amit on 13/10/17.
 */

class Course implements Serializable{

    private static final long serialVersionUID = 1L;

    private String name;
    private String semesterTaken;
    private ArrayList<CourseInstance> courseInstances;
    private int numOfInstances;

    public Course() {
        courseInstances = new ArrayList<>();
    }

    public Course(String name, String semesterTaken) {
        this.name = name;
        this.semesterTaken = semesterTaken;
    }

    public String getName() {
        return name;
    }

    public String getSemesterTaken() {
        return semesterTaken;
    }

    public int getNumOfInstances() {
        return numOfInstances;
    }

    public CourseInstance getInstanceAtIndex(int index) {
        return courseInstances.get(index);
    }

    public void addInstance(CourseInstance courseInstance) {
        courseInstances.add(courseInstance);
        numOfInstances++;
    }

    public void removeInstance(CourseInstance courseInstance) {
        courseInstances.remove(courseInstance);
        if(numOfInstances != 0)
            numOfInstances--;
    }

    public ArrayList<CourseInstance> getCourseInstances() {
        return courseInstances;
    }

    @Override
    public boolean equals(Object obj) {
        Course temp = (Course)obj;
        return name.equals(temp.getName());
    }
}
