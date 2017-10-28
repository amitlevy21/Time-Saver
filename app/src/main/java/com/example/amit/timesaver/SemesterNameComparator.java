package com.example.amit.timesaver;

import java.util.Comparator;

/**
 * Created by amit on 28/10/17.
 */

class SemesterNameComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
        int semesterYear1 = Integer.parseInt(s1.substring(0, 4));
        Semester.eSemesterType semesterType1 = Semester.eSemesterType.valueOf(String.valueOf(s1.charAt(7)));

        int semesterYear2 = Integer.parseInt(s2.substring(0, 4));
        Semester.eSemesterType semesterType2 = Semester.eSemesterType.valueOf(String.valueOf(s2.charAt(7)));

        if(semesterYear1 == semesterYear2) {
            if(semesterType1.equals(semesterType2)) {
                return 0;
            }
            return semesterType2.ordinal() - semesterType1.ordinal();
        }
        return  semesterYear2 - semesterYear1;

    }
}
