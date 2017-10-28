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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class AddCourseActivity extends BaseActivity {

    private ArrayList<Semester> semesters;
    private ArrayList<Course> courses;
    private Course course;
    private String semesterSelected;
    private ArrayList<String> semestersSpinner;

    private String userID;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        buildDrawer();
        DrawerLayout.LayoutParams dlp = (DrawerLayout.LayoutParams) findViewById(R.id.activity_add_course).getLayoutParams();
        dlp.setMargins(50, 50, 50, 50);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();

        semesters = new ArrayList<>();
        courses = new ArrayList<>();
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

        if (getIntent().getSerializableExtra(Keys.SEMESTERS) != null)
            semesters = (ArrayList<Semester>) getIntent().getSerializableExtra(Keys.SEMESTERS);
        DatabaseReference semestersReference = databaseReference.child("users").child(userID).child("semesters").getRef();
        semestersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "read add course", Toast.LENGTH_SHORT).show();
                GenericTypeIndicator<HashMap<String,Semester>> t = new GenericTypeIndicator<HashMap<String,Semester>>() {};

                HashMap<String,Semester> semesterFromFireBase = dataSnapshot.getValue(t);

                for(Map.Entry<String, Semester> entry : semesterFromFireBase.entrySet()) {
                    semestersSpinner.add(entry.getValue().getName());
                }
                Collections.sort(semestersSpinner, new SemesterNameComparator());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        if (!(courseName.compareTo("") == 0) &&
                !(semesterSelected.compareTo("") == 0))
            return true;
        Toast.makeText(getApplicationContext(), "you should enter a course name!", Toast.LENGTH_LONG).show();
        return false;
    }

    private void putInArray(String courseName, String semesterSelected) {
        Semester theSemester = findSemesterByName(semesterSelected);
        course = new Course(courseName, theSemester);
        theSemester.addCourse(course);

        courses.add(course);
        databaseReference.child("users").child(userID).child("semesters").push().
                child("courses").push().setValue(course);
    }

    public Semester findSemesterByName(String nameSemester) {
        int semesterYear = Integer.parseInt(semesterSelected.substring(0, 4));
        Semester.eSemesterType semesterType = Semester.eSemesterType.valueOf(String.valueOf(semesterSelected.charAt(7)));
        for (int i = 0; i < semesters.size(); i++) {
            if ((semesters.get(i).getYear() == semesterYear) &&
                    (semesters.get(i).getSemesterType().equals(semesterType))) {
                return semesters.get(i);
            }
        }
        return null;
    }
}

