package com.example.amit.timesaver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.util.Calendar;

public class AddCourseInstanceActivity extends AppCompatActivity implements RadialTimePickerDialogFragment.OnTimeSetListener,
        CalendarDatePickerDialogFragment.OnDateSetListener{


    private Course choosenCourse;
    private eDay choosenDay;
    private int choosenStartHour;
    private int choosenEndHour;
    private String choosenProffesorName;


    private static final String FRAG_TAG_TIME_PICKER_START = "timePickerDialogFragmentStart";
    private static final String FRAG_TAG_TIME_PICKER_END = "timePickerDialogFragmentEnd";
    private static final String FRAG_DATE_PICKER = "datePickerDialogFragment";

    private enum eDay {
        SUNDAY, MONDAY, TUESDAY, WEDNESSDAY, THURSDAY, FRIDAY, SATURDAY
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instance);

        Bundle bundleFromAddCourses = getIntent().getExtras();

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
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute)  {

        switch (dialog.getTag()) {
            case FRAG_TAG_TIME_PICKER_START: {
                choosenStartHour = hourOfDay * 100 + minute;
                TextView startHourButton = (TextView) findViewById(R.id.start_hour_button);
                startHourButton.setText(hourOfDay + ":" + minute);
                break;
            }
            case FRAG_TAG_TIME_PICKER_END: {
                choosenEndHour = hourOfDay * 100 + minute;
                TextView endHourButton = (TextView) findViewById(R.id.end_hour_button);
                endHourButton.setText(hourOfDay + ":" + minute);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        choosenDay = eDay.values()[dayOfMonth % 7];
        TextView dayTaken = (TextView) findViewById(R.id.day_taken_button);
        dayTaken.setText(choosenDay.name());
    }

}
