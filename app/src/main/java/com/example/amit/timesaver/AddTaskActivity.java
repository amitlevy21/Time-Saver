package com.example.amit.timesaver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

public class AddTaskActivity extends BaseActivity implements CalendarDatePickerDialogFragment.OnDateSetListener {


    private String courseNameSelected;
    private MyDate dueDate;

    private String FRAG_DATE_PICKER = "Date picker";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

//        Bundle bundle = getIntent().getExtras();
//        semesterRelated = (Semester) bundle.getSerializable(Keys.SEMESTER);


        //i meant here to copy references not values
        ArrayList<Course> courses = Dashboard.getInstance().getCourses();
        ArrayList<String> names = new ArrayList<>();

        for(Course c: courses) {
            if(!names.contains(c.getName()))
            names.add(c.getName());
        }


        ArrayAdapter<String> coursesNames = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        Spinner coursesSpinner = findViewById(R.id.add_task_course_spinner);

        coursesNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        coursesSpinner.setAdapter(coursesNames);

        coursesSpinner.setSelection(0);

        addListeners(coursesSpinner);

    }

    private void addListeners(Spinner coursesSpinner) {
        coursesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelection(i);
                courseNameSelected = (String) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(0);
                courseNameSelected = (String) adapterView.getItemAtPosition(0);
            }
        });

        Button setDueDateButton = findViewById(R.id.add_task_due_date_button);
        setDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(AddTaskActivity.this)
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setDoneText("Select")
                        .setCancelText("Cancel");
                cdp.show(getSupportFragmentManager(), FRAG_DATE_PICKER);
            }
        });
        Button confirm = findViewById(R.id.add_task_confirm_button);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText description = findViewById(R.id.task_description);
                Course courseRelated = getCourseByName(courseNameSelected);

                Task taskToAdd = new Task(courseRelated, dueDate, description.getText().toString());
                Intent intent = new Intent(getApplicationContext(), TaskManagerActivity.class);
                intent.putExtra(Keys.TASK_ADDED, taskToAdd);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private Course getCourseByName(String courseNameSelected) {
        ArrayList<Course> courses = Dashboard.getInstance().getCourses();
        for (Course c: courses) {
            if(c.getName().equals(courseNameSelected)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        dueDate = new MyDate(year, monthOfYear, dayOfMonth);
    }
}
