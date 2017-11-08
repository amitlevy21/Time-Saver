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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;


public class AddCourseInstanceActivity extends BaseActivity implements RadialTimePickerDialogFragment.OnTimeSetListener,
        CalendarDatePickerDialogFragment.OnDateSetListener {


    private static final int INDEX_OF_INSTANCE_NOT_FOUND = -1;
    private static final int INDEX_OF_COURSE_NOT_FOUND = -1;
    private String chosenSemester;
    private String chosenCourse;
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
    private ArrayList<String> semestersNames;
    private Spinner spinSemester;
    private ArrayAdapter<String> adapterCourseSpinner;
    private ArrayAdapter<String> adapterSemesterSpinner;
    private Spinner spinCourses;
    private ArrayList<Semester> semesters;
    private MyDate date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instance);

        buildDrawer();
        DrawerLayout.LayoutParams dlp = (DrawerLayout.LayoutParams) findViewById(R.id.activity_add_instance).getLayoutParams();
        dlp.setMargins(50, 50, 50, 50);

        courseInstances = new ArrayList<>();
        coursesNames = new ArrayList<>();
        semestersNames = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userID = currentUser.getUid();
        }

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
        semesters = Dashboard.getInstance().getSemesters();
        courses = Dashboard.getInstance().getCourses();
        for (Course c: courses) {
            HashMap<String, CourseInstance> instances = c.getCourseInstances();
            for(CourseInstance courseInstance: instances.values()) {
                if(!courseInstances.contains(courseInstance))
                    courseInstances.add(courseInstance);
            }
        }
        spinCourses = findViewById(R.id.course_spinner);

        /*if (getIntent().getSerializableExtra(Keys.COURSES) != null)
            courses = (ArrayList<Course>) getIntent().getSerializableExtra(Keys.COURSES);*/

        spinSemester = findViewById(R.id.add_instance_spinner_semester);

        spinSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);
                chosenSemester = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(0);
                chosenSemester = adapterView.getSelectedItem().toString();
            }
        });

        spinCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);
                chosenCourse = adapterView.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(0);
                chosenCourse = adapterView.getSelectedItem().toString();
            }
        });

        for (Course c: courses) {
            if(!coursesNames.contains(c.getName()))
                coursesNames.add(c.getName());
        }

        for (Semester s: semesters) {
            if(!semestersNames.contains(s.getName()))
                semestersNames.add(s.getName());
        }

        adapterSemesterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, semestersNames);
        adapterCourseSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, coursesNames);

        spinCourses.setAdapter(adapterCourseSpinner);

        spinSemester.setAdapter(adapterSemesterSpinner);

    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {

        switch (dialog.getTag()) {
            case FRAG_TAG_TIME_PICKER_START: {
                chosenStartHour = hourOfDay * 100 + minute;
                TextView startHourButton = findViewById(R.id.start_hour_button);
                String hourToDisplay;
                if (chosenStartHour < 1000) {
                    if (minute < 10)
                        hourToDisplay = "0" + hourOfDay + ":0" + minute;
                    else
                        hourToDisplay = "0" + hourOfDay + ":" + minute;
                } else
                    hourToDisplay = String.valueOf(chosenStartHour);
                startHourButton.setText(String.valueOf(hourToDisplay));
                break;
            }
            case FRAG_TAG_TIME_PICKER_END: {
                if (hourOfDay * 100 + minute > chosenStartHour) {
                    chosenEndHour = hourOfDay * 100 + minute;
                    TextView endHourButton = findViewById(R.id.end_hour_button);
                    String hourToDisplay;
                    if (chosenEndHour < 1000) {
                        if (minute < 10)
                            hourToDisplay = "0" + hourOfDay + ":0" + minute;
                        else
                            hourToDisplay = "0" + hourOfDay + ":" + minute;
                    } else
                        hourToDisplay = hourOfDay + ":" + minute;
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
        TextView dayTaken = findViewById(R.id.day_taken_button);
        dayTaken.setText(chosenDay.name());

        date = new MyDate(year, monthOfYear, dayOfMonth);

    }


    private void addListeners() {

        TextView dayTaken = findViewById(R.id.day_taken_button);
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

        TextView startHourButton = findViewById(R.id.start_hour_button);
        startHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //thanks android-better-pickers
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(AddCourseInstanceActivity.this);
                rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER_START);
            }
        });

        TextView endHourButton = findViewById(R.id.end_hour_button);
        endHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //thanks android-better-pickers
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(AddCourseInstanceActivity.this);
                rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER_END);
            }
        });

        final EditText professorNameText = findViewById(R.id.professor_name_input);
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

        Button confirmButton = findViewById(R.id.button_confirm_add_instance);
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

        FloatingActionButton fab = findViewById(R.id.add_instance_fab);


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
        //if user wants to delete instances he will need to erase the entire course or just delete the event in
        // the google calendar

        Query querySemester = databaseReference.child("users").child(userID)
                .child("semesters").orderByChild("name").equalTo(chosenSemester);

        querySemester.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String semesterKey = dataSnapshot.getChildren().iterator().next().getKey();
                    Query queryCourse = dataSnapshot.getRef().child(semesterKey).
                            child("courses").orderByChild("name").equalTo(chosenCourse);
                    queryCourse.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                DatabaseReference courseData = dataSnapshot.getRef();
                                String courseKey = dataSnapshot.getChildren().iterator().next().getKey();
                                Course foundCourse = dataSnapshot.getChildren().iterator().next().getValue(Course.class);

                                foundCourse.removeAllInstances();
                                courseData.child("numOfInstances").setValue(foundCourse.getNumOfInstances());
                                Toast.makeText(getApplicationContext(), "All instances have been deleted", Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private boolean save() {
        final CourseInstance courseInstance;
        final Course course = courses.get(findCourseIndexByName());
        if (chosenProfessorName != null) {
            courseInstance = new CourseInstance
                    (course, date, chosenDay, chosenStartHour, chosenEndHour, chosenProfessorName);
        } else {
            courseInstance = new CourseInstance(course, date, chosenDay, chosenStartHour, chosenEndHour);
        }


        if(!courseInstances.contains(courseInstance)) {
            courseInstances.add(courseInstance);

            Query querySemester = databaseReference.child("users").child(userID)
                    .child("semesters").orderByChild("name").equalTo(chosenSemester);

            querySemester.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String semesterKey = dataSnapshot.getChildren().iterator().next().getKey();
                        Query queryCourse = dataSnapshot.getRef().child(semesterKey).
                                child("courses").orderByChild("name").equalTo(chosenCourse);
                        queryCourse.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    DatabaseReference courseData = dataSnapshot.getRef();
                                    String courseKey = dataSnapshot.getChildren().iterator().next().getKey();
                                    Course foundCourse = dataSnapshot.getChildren().iterator().next().getValue(Course.class);
                                    courseData.child(courseKey).child("instances").push().setValue(courseInstance);
                                    DatabaseReference keyInstanceData = courseData.child(courseKey).child("instances").push();
                                    String instanceKey = keyInstanceData.getKey();
                                    int theCourse = findCourseIndexByName();
                                    courses.get(theCourse).addInstance(instanceKey, courseInstance);
                                    courseData.child(courseKey).child("numOfInstances").setValue(courses.get(theCourse).getNumOfInstances());
                                    Toast.makeText(getApplicationContext(), "Instance successfully added!", Toast.LENGTH_LONG).show();

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

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

    private int findCourseIndexByName() {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getName().equals(chosenCourse))
                return i;
        }
        return INDEX_OF_COURSE_NOT_FOUND;
    }

    private int findInstanceIndex() {
        Course c = courses.get(findCourseIndexByName());
        CourseInstance instanceToFind = new CourseInstance(c, date, chosenDay, chosenStartHour, chosenEndHour, chosenProfessorName);
        for (int i = 0; i < courseInstances.size(); i++) {
            if (courseInstances.get(i).equals(instanceToFind)) {
                return i;
            }
        }
        return INDEX_OF_INSTANCE_NOT_FOUND;
    }
}
