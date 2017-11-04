package com.example.amit.timesaver;

import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class AddCourseInstanceActivity extends BaseActivity implements RadialTimePickerDialogFragment.OnTimeSetListener,
        CalendarDatePickerDialogFragment.OnDateSetListener {


    private static final int INDEX_OF_INSTANCE_NOT_FOUND = -1;
    private Course chosenCourse;
    private CourseInstance.eDay chosenDay;
    private int chosenStartHour;
    private int chosenEndHour;
    private String chosenProfessorName;
    private ArrayList<CourseInstance> courseInstances;
    private ArrayList<Course> courses;


    private static final String FRAG_TAG_TIME_PICKER_START = "timePickerDialogFragmentStart";
    private static final String FRAG_TAG_TIME_PICKER_END = "timePickerDialogFragmentEnd";
    private static final String FRAG_DATE_PICKER = "datePickerDialogFragment";

    private String userID;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ArrayList<String> coursesNames;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instance);

        buildDrawer();
        DrawerLayout.LayoutParams dlp = (DrawerLayout.LayoutParams) findViewById(R.id.activity_add_instance).getLayoutParams();
        dlp.setMargins(50, 50, 50, 50);

        courseInstances = new ArrayList<>();
        courses = new ArrayList<>();
        coursesNames = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();

        addListeners();

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

        if (getIntent().getSerializableExtra(Keys.COURSES) != null)
            courses = (ArrayList<Course>) getIntent().getSerializableExtra(Keys.COURSES);

        DatabaseReference semestersReference = databaseReference.child("users").child(userID).child("courses").getRef();
        semestersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {/*
                Toast.makeText(getApplicationContext(), "instance read", Toast.LENGTH_SHORT).show();

                GenericTypeIndicator<HashMap<String,Course>> t = new GenericTypeIndicator<HashMap<String,Course>>() {};

                HashMap<String,Course> coursesFromFireBase = dataSnapshot.getValue(t);

                for(Map.Entry<String, Course> entry : coursesFromFireBase.entrySet()) {
                    coursesNames.add(entry.getValue().getName());
                }
                Collections.sort(coursesNames);*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, coursesNames);
        Spinner spinCourses = (Spinner) findViewById(R.id.course_spinner);
        spinCourses.setAdapter(adapter);
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {

        switch (dialog.getTag()) {
            case FRAG_TAG_TIME_PICKER_START: {
                chosenStartHour = hourOfDay * 100 + minute;
                TextView startHourButton = (TextView) findViewById(R.id.start_hour_button);
                String hourToDisplay;
                if (chosenStartHour < 1000) {
                    if(minute < 10)
                        hourToDisplay = "0" + hourOfDay + ":0" + minute;
                    else
                        hourToDisplay = "0" + hourOfDay + ":" + minute;
                }
                else
                    hourToDisplay = String.valueOf(chosenStartHour);
                startHourButton.setText(String.valueOf(hourToDisplay));
                break;
            }
            case FRAG_TAG_TIME_PICKER_END: {
                if (hourOfDay * 100 + minute > chosenStartHour) {
                    chosenEndHour = hourOfDay * 100 + minute;
                    TextView endHourButton = (TextView) findViewById(R.id.end_hour_button);
                    String hourToDisplay;
                    if (chosenEndHour < 1000) {
                        if(minute < 10)
                            hourToDisplay = "0" + hourOfDay + ":0" + minute;
                        else
                            hourToDisplay = "0" + hourOfDay + ":" + minute;
                    }
                    else
                        hourToDisplay = String.valueOf(chosenEndHour);
                    endHourButton.setText(String.valueOf(hourToDisplay));
                } else {
                    Toast.makeText(getApplicationContext(), "Please choose valid hours", Toast.LENGTH_LONG).show();
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

        Calendar myCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

        int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);

        chosenDay = CourseInstance.eDay.values()[dayOfWeek - 1];
        TextView dayTaken = (TextView) findViewById(R.id.day_taken_button);
        dayTaken.setText(chosenDay.name());

    }


    private void addListeners() {

        Spinner spinCourses = (Spinner) findViewById(R.id.course_spinner);

        spinCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenCourse = courses.get(i);
                adapterView.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chosenCourse = courses.get(0);
                adapterView.setSelection(0);
            }
        });

        TextView dayTaken = (TextView) findViewById(R.id.day_taken_button);
        dayTaken.setOnClickListener(new View.OnClickListener() {
            //thanks android-better-pickers
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(AddCourseInstanceActivity.this)
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setDoneText("Select")
                        .setCancelText("Cancel");
                cdp.show(getSupportFragmentManager(), FRAG_DATE_PICKER);
            }
        });

        TextView startHourButton = (TextView) findViewById(R.id.start_hour_button);
        startHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //thanks android-better-pickers
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(AddCourseInstanceActivity.this);
                rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER_START);
            }
        });

        TextView endHourButton = (TextView) findViewById(R.id.end_hour_button);
        endHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //thanks android-better-pickers
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(AddCourseInstanceActivity.this);
                rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER_END);
            }
        });

        final EditText professorNameText = (EditText) findViewById(R.id.professor_name_input);
        professorNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                chosenProfessorName = editable.toString();
            }
        });

        Button confirmButton = (Button) findViewById(R.id.button_confirm_add_instance);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (save()) {
                    Intent intentDashboard = new Intent(getApplicationContext(), DashboardActivity.class);
                    intentDashboard.putExtra(Keys.COURSE_INSTANCES, courseInstances);
                    startActivity(intentDashboard);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_instance_fab);


        //add course
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput())
                    save();
            }
        });

        //remove course
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (checkInput())
                    delete();
                return true;
            }
        });
    }


    private void delete() {
        int index = findInstanceByIndex();
        if(index != INDEX_OF_INSTANCE_NOT_FOUND) {
            // TODO: 11/2/2017 remove from firebase
            courseInstances.remove(index);
        }
    }



    // TODO: 11/2/2017 fix firebase
    private boolean save() {
        CourseInstance courseInstance;
        if (chosenProfessorName != null) {
            courseInstance = new CourseInstance
                    (chosenCourse, chosenDay, chosenStartHour, chosenEndHour, chosenProfessorName);
        } else {
            courseInstance = new CourseInstance(chosenCourse, chosenDay, chosenStartHour, chosenEndHour);
        }
        courseInstances.add(courseInstance);
       // databaseReference.child("users").child(userID).child("semesters")
       //         .push().child("courses").push().child("instances").push().setValue(courseInstance);
        return true;
    }


    private boolean checkInput() {
        if (chosenCourse != null && chosenDay != null && chosenStartHour != chosenEndHour && chosenStartHour != 0 && chosenEndHour != 0) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Please fill all the fields before continuing", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
    }

    private int findInstanceByIndex() {
        CourseInstance instanceToFind = new CourseInstance(chosenCourse, chosenDay, chosenStartHour, chosenEndHour, chosenProfessorName);
        for (int i = 0; i < courseInstances.size(); i++) {
            if (courseInstances.get(i).equals(instanceToFind)) {
                return i;
            }
        }
        return INDEX_OF_INSTANCE_NOT_FOUND;
    }
}
