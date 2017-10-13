package com.example.amit.timesaver;

import java.util.ArrayList;

/**
 * Created by amit on 13/10/17.
 */

class Course {

    private String name;
    private Semester semesterTaken;
    private ArrayList<CourseInstance> courseInstances = new ArrayList<>();
    private int numOfInstances;

    public Course(String name, Semester semesterTaken) {
        this.name = name;
        this.semesterTaken = semesterTaken;
    }

    public String getName() {
        return name;
    }

    public Semester getSemesterTaken() {
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
}
