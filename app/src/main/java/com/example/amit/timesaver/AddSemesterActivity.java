package com.example.amit.timesaver;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddSemesterActivity extends BaseActivity implements CalendarDatePickerDialogFragment.OnDateSetListener {

    private static final String FRAG_DATE_PICKER_START = "frag_date_picker_start";
    private static final String FRAG_DATE_PICKER_END = "frag_date_picker_end";
    private static final int INTERVAL_YEAR = 10;
    private static final int SEMESTER_INDEX_NOT_FOUND = -1;

    private ArrayList<Semester> semestersToAdd;
    private Dashboard dashboard;

    private int yearSelected;
    private MyDate startDateSelected;
    private MyDate endDateSelected;
    private Semester.eSemesterType semesterTypeSelected;

    private String userID;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_semester);

        buildDrawer();
        DrawerLayout.LayoutParams dlp = (DrawerLayout.LayoutParams) findViewById(R.id.activity_add_semester).getLayoutParams();
        dlp.setMargins(50, 50, 50, 50);

        dashboard = Dashboard.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userID = currentUser.getUid();
        }

        semestersToAdd = dashboard.getSemesters();

        ArrayList<String> years = new ArrayList<>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear - INTERVAL_YEAR; i <= thisYear + INTERVAL_YEAR; i++) {
            years.add(Integer.toString(i));
        }

        setListeners(years);

    }


    private void setListeners(ArrayList<String> years) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);

        Spinner spinYear = findViewById(R.id.year_spinner);
        spinYear.setAdapter(adapter);
        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);
                yearSelected = Integer.parseInt(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(adapterView.getLastVisiblePosition());
                yearSelected = Integer.parseInt(adapterView.getSelectedItem().toString());
            }
        });

        final Spinner spinSemesterType = findViewById(R.id.semester_type_spinner);
        spinSemesterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);
                semesterTypeSelected = Semester.eSemesterType.values()[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(0);
                semesterTypeSelected = Semester.eSemesterType.values()[0];
            }
        });

        TextView semesterStartDate = findViewById(R.id.start_date_text_view);
        semesterStartDate.setOnClickListener(new View.OnClickListener() {
            //thanks android-better-pickers
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(AddSemesterActivity.this)
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setDoneText("Select")
                        .setCancelText("Cancel");
                cdp.show(getSupportFragmentManager(), FRAG_DATE_PICKER_START);
            }
        });

        TextView semesterEndDate = findViewById(R.id.end_date_text_view);
        semesterEndDate.setOnClickListener(new View.OnClickListener() {
            //thanks android-better-pickers
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(AddSemesterActivity.this)
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setDoneText("Select")
                        .setCancelText("Cancel");
                cdp.show(getSupportFragmentManager(), FRAG_DATE_PICKER_END);
            }
        });

        Button confirmButton = findViewById(R.id.button_confirm_add_semester);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    Semester semester = new Semester(yearSelected, startDateSelected, endDateSelected, semesterTypeSelected);
                    semestersToAdd.add(semester);
                    databaseReference.child("users").child(userID).child("semesters").push().setValue(semester);


                    Intent intentEnterCourses = new Intent(getApplicationContext(), AddCourseActivity.class);
                    intentEnterCourses.putExtra(Keys.SEMESTERS, semestersToAdd);
                    startActivity(intentEnterCourses);
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.add_semester_fab);

        //add semester
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    Semester semester = new Semester(yearSelected, startDateSelected, endDateSelected, semesterTypeSelected);
                    semestersToAdd.add(semester);
                    databaseReference.child("users").child(userID).child("semesters").push().setValue(semester);

                    Toast.makeText(getApplicationContext(), "Semester successfully added!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //remove semester
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int indexOfSemester = findSemesterIIndexByInput();
                if (indexOfSemester != SEMESTER_INDEX_NOT_FOUND) {
                    semestersToAdd.remove(indexOfSemester);
                    String semesterNameToFind = yearSelected + " - " + semesterTypeSelected.name();
                    Query findSemester = databaseReference.child("users").child(userID).child("semesters").orderByChild("name").equalTo(semesterNameToFind);
                    findSemester.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                DatabaseReference semesterData = dataSnapshot.getRef();
                                String semesterKey = dataSnapshot.getChildren().iterator().next().getKey();
                                Semester foundSemester = dataSnapshot.getChildren().iterator().next().getValue(Semester.class);
                                semesterData.child(semesterKey).removeValue();

                                Toast.makeText(getApplicationContext(), "Semester  successfully erased!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


                return true;
            }

            private int findSemesterIIndexByInput() {
                for (int i = 0; i < semestersToAdd.size(); i++) {
                    Semester s = new Semester(yearSelected, startDateSelected, endDateSelected, semesterTypeSelected);
                    if (semestersToAdd.get(i).equals(s))
                        return i;

                }
                return SEMESTER_INDEX_NOT_FOUND;
            }
        });
    }


    private boolean checkInput() {
        if (startDateSelected != null && endDateSelected != null) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Please choose start and end dates for the semester", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
    }


    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        MyDate myDate = new MyDate(year, monthOfYear, dayOfMonth);
        if (dialog.getTag().equals(FRAG_DATE_PICKER_START)) {
            startDateSelected = myDate;
            TextView spinSemesterStartDate = findViewById(R.id.start_date_text_view);
            spinSemesterStartDate.setText(myDate.toString());
        } else {
            endDateSelected = myDate;
            TextView spinSemesterEndDate = findViewById(R.id.end_date_text_view);
            spinSemesterEndDate.setText(myDate.toString());
        }
    }


}

