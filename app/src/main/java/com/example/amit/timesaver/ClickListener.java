package com.example.amit.timesaver;

import android.view.View;

/**
 * Created by levyamit on 10/25/2017.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
