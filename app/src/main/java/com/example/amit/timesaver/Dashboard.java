package com.example.amit.timesaver;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by amit on 13/10/17.
 */

public class Dashboard implements Serializable{

    private static final long serialVersionUID = 1L;

    private int numOfTodaysTasksDue;
    private int numOfTodaysClasses;

    private int numOfTomorrowsTasksDue;
    private int numOfTomorrowsClasses;

    private int numOfTotalTasksDone;

    private static Dashboard dashboardInstance = new Dashboard();

    private ArrayList<Semester> semesters = new ArrayList<>();
    private ArrayList<Task> tasks = new ArrayList<>();


    private Dashboard() {}

    public static Dashboard getInstance() {
        return dashboardInstance;
    }

    public int getNumOfTodaysTasksDue() {
        return numOfTodaysTasksDue;
    }

    public void setNumOfTodaysTasksDue(int numOfTodaysTasksDue) {
        this.numOfTodaysTasksDue = numOfTodaysTasksDue;
    }

    public int getNumOfTodaysClasses() {
        return numOfTodaysClasses;
    }

    public void setNumOfTodaysClasses(int numOfTodaysClasses) {
        this.numOfTodaysClasses = numOfTodaysClasses;
    }

    public int getNumOfTomorrowsTasksDue() {
        return numOfTomorrowsTasksDue;
    }

    public void setNumOfTomorrowsTasksDue(int numOfTomorrowsTasksDue) {
        this.numOfTomorrowsTasksDue = numOfTomorrowsTasksDue;
    }

    public int getNumOfTomorrowsClasses() {
        return numOfTomorrowsClasses;
    }

    public void setNumOfTomorrowsClasses(int numOfTomorrowsClasses) {
        this.numOfTomorrowsClasses = numOfTomorrowsClasses;
    }

    public int getNumOfTotalTasksDone() {
        return numOfTotalTasksDone;
    }

    public void setNumOfTotalTasksDone(int numOfTotalTasksDone) {
        this.numOfTotalTasksDone = numOfTotalTasksDone;
    }

    public ArrayList<Semester> getSemesters() {
        return semesters;
    }

    public ArrayList<Course> getCourses() {
        ArrayList<Course> toReturn = new ArrayList<>(semesters.size());
        for (Semester s: semesters) {
            toReturn.addAll(s.getCourses().values());
        }
        return toReturn;
    }

    public void setSemesters(ArrayList<Semester> semesters) {
        this.semesters = semesters;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
