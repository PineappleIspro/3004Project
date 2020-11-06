package com.example.wiffle_adeel;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class Planner extends AppCompatActivity {

    // Calendar object
    CalendarView calender;
    // Text object for displaying date
    TextView date_view;
    // List object to store data for specified dates
    Map calendar_data;
    // EditText object to allow input for the above List
    EditText textbx;
    String Date = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        // ID references the id's defined in activity_main.xml
        calender = (CalendarView)
                findViewById(R.id.calender);
        date_view = (TextView)
                findViewById(R.id.date_view);
        textbx = (EditText)
                findViewById(R.id.calender_data);

        // Set up Calender listener
        calender
                .setOnDateChangeListener(
                        new CalendarView
                                .OnDateChangeListener() {
                            @Override

                            // Gets the value of day/month/year on every date change
                            public void onSelectedDayChange(
                                    @NonNull CalendarView view,
                                    int year,
                                    int month,
                                    int dayOfMonth)
                            {
                                // Assigns the selected date to the Date string
                                // month index starts at 0 hence the + 1
                                Date
                                        = dayOfMonth + "-"
                                        + (month + 1) + "-" + year;
                                // set this date in TextView for Display
                                date_view.setText(Date);
                                // read the text associated with this date in the Map
                                //TODO
                                //textbx.setText((String) calendar_data.get(Date));
                                textbx.setText(Date); //placeholder to test assignment
                            }
                        });

        // Set up EditText listener
        //TODO
        textbx
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //calendar_data.put(Date, s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
    }
}