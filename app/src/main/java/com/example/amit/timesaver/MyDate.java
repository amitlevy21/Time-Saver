package com.example.amit.timesaver;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by amit on 13/10/17.
 */

class MyDate implements Comparable<MyDate> , Serializable{

    private static final long serialVersionUID = 1L;

    private int year;
    private int month;
    private int day;

    MyDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public MyDate() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public int compareTo(@NonNull MyDate myDate) {
        if(year != myDate.year) {
            return year - myDate.year;
        }
        else {
            if(month != myDate.month) {
                return month - myDate.month;
            }
            else {
                return day - myDate.day;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MyDate) {
            MyDate temp = (MyDate)obj;
            return day == temp.day && month == temp.month && year == temp.year;
        }
        return false;
    }

    @Override
    public String toString() {
        return day + "/" + (month+ 1) + "/" + year;
    }
}
