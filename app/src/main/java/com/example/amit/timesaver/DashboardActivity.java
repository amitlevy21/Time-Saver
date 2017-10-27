package com.example.amit.timesaver;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class DashboardActivity extends BaseActivity {

    private Dashboard dashboard = Dashboard.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        buildDrawer();

        int[] quantityToday = {dashboard.getNumOfTodaysTasksDue(), dashboard.getNumOfTodaysClasses(), dashboard.getNumOfTotalTasksDone()};
        int[] quantityTomorrow = {dashboard.getNumOfTomorrowsTasksDue(), dashboard.getNumOfTomorrowsClasses()};

        TableLayout tableLayoutToday = (TableLayout) findViewById(R.id.dashboard_table_today);

        for (int i = 0; i < quantityToday.length; i++) {
            TableRow row = (TableRow) tableLayoutToday.getChildAt(i);
            TextView textView = (TextView) row.getChildAt(1);
            textView.setText(String.valueOf(quantityToday[i]));

        }

        TableLayout tableLayoutTomorrow = (TableLayout) findViewById(R.id.dashboard_table_tomorrow);

        for (int i = 0; i < quantityTomorrow.length; i++) {
            TableRow row = (TableRow) tableLayoutTomorrow.getChildAt(i);
            TextView textView = (TextView) row.getChildAt(1);
            textView.setText(String.valueOf(quantityToday[i]));
        }


    }
}