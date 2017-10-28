package com.example.amit.timesaver;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.util.ArrayList;

public class AddSemesterActivity extends BaseActivity implements CalendarDatePickerDialogFragment.OnDateSetListener {

    private static final String FRAG_DATE_PICKER_START = "frag_date_picker_start";
    private static final String FRAG_DATE_PICKER_END = "frag_date_picker_end";

    private ArrayList<Semester> semesters = new ArrayList<>();

    private int yearSelected;
    private MyDate startDateSelected;
    private MyDate endDateSelected;
    private Semester.eSemesterType semesterTypeSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_semester);

        buildDrawer();
        DrawerLayout.LayoutParams dlp  = (DrawerLayout.LayoutParams)findViewById(R.id.activity_add_semester).getLayoutParams();
        dlp.setMargins(50,50,50,50);

        ArrayList<String> years = new ArrayList<>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1970; i <= thisYear + 10; i++) {
            years.add(Integer.toString(i));
        }

        setListeners(years);

    }

    private void setListeners(ArrayList<String> years) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);

        Spinner spinYear = (Spinner)findViewById(R.id.year_spinner);
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

        Spinner spinSemesterType = (Spinner) findViewById(R.id.semester_type_spinner);
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

        TextView spinSemesterStartDate = (TextView) findViewById(R.id.start_date_text_view);
        spinSemesterStartDate.setOnClickListener(new View.OnClickListener() {
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

        TextView spinSemesterEndDate = (TextView) findViewById(R.id.end_date_text_view);
        spinSemesterEndDate.setOnClickListener(new View.OnClickListener() {
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

        Button confirmButton = (Button) findViewById(R.id.button_confirm_add_semester);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInputBeforeNextActivity()) {
                    Intent intentEnterCourses = new Intent(getApplicationContext(), AddCourseActivity.class);
                    intentEnterCourses.putExtra(Keys.SEMESTERS, semesters);
                    startActivity(intentEnterCourses);
                }
            }
        });

        Button addSemesterButton = (Button) findViewById(R.id.button_add_semester);
        addSemesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInputBeforeNextActivity())
                    Toast.makeText(getApplicationContext(), "Semester successfully added!", Toast.LENGTH_LONG).show();
            }
        });
    }


    private boolean checkInputBeforeNextActivity() {
        if(startDateSelected != null && endDateSelected != null) {
            Semester semester = new Semester(yearSelected, startDateSelected, endDateSelected, semesterTypeSelected);
            semesters.add(semester);
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
        if(dialog.getTag().equals(FRAG_DATE_PICKER_START)) {
            startDateSelected = myDate;
            TextView spinSemesterStartDate = (TextView) findViewById(R.id.start_date_text_view);
            spinSemesterStartDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
        } else {
            endDateSelected = myDate;
            TextView spinSemesterEndDate = (TextView) findViewById(R.id.end_date_text_view);
            spinSemesterEndDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
        }
    }



}

