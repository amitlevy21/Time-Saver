package com.example.amit.timesaver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class AddCourseActivity extends BaseActivity {

    private ArrayList<Semester> semesters = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private Course course;
    private Semester semester;
    private String semesterSelected;
    private ArrayList<String> semestersSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        buildDrawer();
        DrawerLayout.LayoutParams dlp  = (DrawerLayout.LayoutParams)findViewById(R.id.activity_add_course).getLayoutParams();
        dlp.setMargins(50,50,50,50);

        semestersSpinner = new ArrayList<>();

        setListeners();
    }

    //added because of activity launchMode is SingleTop
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        //now getIntent() should always return the last received intent
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getSerializableExtra(Keys.SEMESTERS) != null)
            semesters = (ArrayList<Semester>) getIntent().getSerializableExtra(Keys.SEMESTERS);

        for (int i = 0; i < semesters.size(); i++) {
            String semesterNameFormat = semesters.get(i).getYear() + " - " + semesters.get(i).getSemesterTypeArr();
            semestersSpinner.add(semesterNameFormat);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, semestersSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinSemester = (Spinner) findViewById(R.id.semester_spinner);
        spinSemester.setAdapter(adapter);

    }

    private void setListeners() {

        Spinner spinSemester = (Spinner) findViewById(R.id.semester_spinner);
        spinSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);
                semesterSelected = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(adapterView.getLastVisiblePosition());
                semesterSelected = adapterView.getSelectedItem().toString();
            }
        });


        Button confirmCourseButton = (Button) findViewById(R.id.button_confirm_add_course);
        confirmCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText txtDescription = (EditText) findViewById(R.id.course_name_input);
                String courseName = txtDescription.getText().toString();
                if (checkInput(courseName)) {
                    putInArray(courseName, semesterSelected);

                    Intent intentEnterCourses = new Intent(getApplicationContext(), AddCourseInstanceActivity.class);
                    intentEnterCourses.putExtra(Keys.COURSES, courses);
                    startActivity(intentEnterCourses);
                }
            }
        });

        Button addAnotherCourseButton = (Button) findViewById(R.id.button_add_another_course);
        addAnotherCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText txtDescription = (EditText) findViewById(R.id.course_name_input);
                String courseName = txtDescription.getText().toString();
                if (checkInput(courseName)) {
                    putInArray(courseName, semesterSelected);
                    Toast.makeText(getApplicationContext(), "Course successfully added!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private boolean checkInput(String courseName) {
        if(!(courseName.compareTo("") == 0) &&
                !(semesterSelected.compareTo("") == 0))
            return true;
        Toast.makeText(getApplicationContext(), "you should enter a course name!", Toast.LENGTH_LONG).show();
        return false;
    }

    private void putInArray(String courseName, String semesterSelected) {
        int semesterYear = Integer.parseInt(semesterSelected.substring(0, 4));
        char semesterType = semesterSelected.charAt(7);
        for (int i = 0; i < semesters.size(); i++) {
            if ((semesters.get(i).getYear() == semesterYear) &&
                    (semesters.get(i).getSemesterTypeArr().toString().compareTo(Character.toString(semesterType)) == 0)) {
                course = new Course(courseName, semesters.get(i));
                break;
            }
        }
            courses.add(course);
    }
}

