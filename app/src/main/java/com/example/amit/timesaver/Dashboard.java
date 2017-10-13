package com.example.amit.timesaver;

/**
 * Created by amit on 13/10/17.
 */

public class Dashboard {

    private int numOfTodaysTasksDue;
    private int numOfTodaysClasses;

    private int numOfTomorrowsTasksDue;
    private int numOfTomorrowsClasses;

    private int numOfTotalTasksDone;

    private static Dashboard dashboardInstance = new Dashboard();

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
}
