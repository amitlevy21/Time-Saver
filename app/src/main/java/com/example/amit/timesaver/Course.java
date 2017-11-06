package com.example.amit.timesaver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by amit on 13/10/17.
 */

class Course implements Serializable{

    private static final long serialVersionUID = 1L;

    private String name;
    private HashMap<String, CourseInstance> courseInstances = new HashMap<>();
    private int numOfInstances;

    public Course() {
    }

    public Course(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getNumOfInstances() {
        return numOfInstances;
    }


    public CourseInstance getInstanceAtIndex(int index) {
        return courseInstances.get(index);
    }

    public void addInstance(String courseKey, CourseInstance courseInstance) {
        courseInstances.put(courseKey, courseInstance);
        numOfInstances++;
    }

    public void removeInstance(CourseInstance courseInstance) {
        courseInstances.remove(courseInstance);
        if(numOfInstances != 0)
            numOfInstances--;
    }

    public HashMap<String, CourseInstance> getCourseInstances() {
        return courseInstances;
    }

    public ArrayList<CourseInstance> getArrayListCourseInstances() {
        ArrayList<CourseInstance> returnInstance = new ArrayList<>(courseInstances.size());
        for(CourseInstance courseInstance: courseInstances.values()) {
            returnInstance.add(courseInstance);
        }
        return  returnInstance;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Course) {
            Course temp = (Course) obj;
            return name.equals(temp.getName());
        }
        return false;
    }
}
