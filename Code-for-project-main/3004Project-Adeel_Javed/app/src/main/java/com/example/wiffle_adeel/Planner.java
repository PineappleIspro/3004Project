package com.example.wiffle_adeel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Planner extends AppCompatActivity {

    // private LocalDate currentDate = LocalDate.now();
    private static String date;

    //Setting up the persistent SharedPreferences objects to store planner data
    protected static SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    //Requests the Planner Data matching the String date, returns an empty string "" if no data
    //Use this format for the date string: "DD-MM-YYYY"
    public static String getPlannerData(String date) {
        return sharedPref.getString(date, "");
    }

    // Calendar object
    private CalendarView calender;
    // Text object for displaying date
    private TextView date_view;
    // Button for inputting schedule data
    private Button calendar_button;

    // Clock for selecting the time for the planner event
    private TimePicker clock;
    // Textbox for modifying planner data, possibly temporary until better implementation
    private EditText textbx;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        //Initializes sharedPreferences
        sharedPref = getSharedPreferences("com.wiffleweather.android.sharedpref", MODE_PRIVATE);
        editor = sharedPref.edit();

        // ID references the id's defined in activity_main.xml
        calender = (CalendarView)
                findViewById(R.id.calender);
        date_view = (TextView)
                findViewById(R.id.date_view);
        calendar_button = (Button)
                findViewById(R.id.calender_button);
        Button back_Button = (Button)
                findViewById(R.id.back_button);
        clock = (TimePicker)
                findViewById(R.id.timeSelector);
        clock.setIs24HourView(true);

        textbx = (EditText)
                findViewById(R.id.calender_data);

        back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });


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
                                    int dayOfMonth) {
                                // Assigns the selected date to the Date string
                                // month index starts at 0 hence the + 1
                                // Adds preceding 0 if applicable
                                String s_month;
                                if (month < 10){
                                    s_month = ("0" + String.valueOf((month+1)));
                                } else {
                                    s_month = (String.valueOf((month+1)));
                                }

                                String s_dayOfMonth;
                                if (dayOfMonth < 10){
                                    s_dayOfMonth = ("0" + String.valueOf((dayOfMonth)));
                                } else {
                                    s_dayOfMonth = String.valueOf((dayOfMonth));
                                }

                                date
                                        = s_dayOfMonth + "-"
                                        + s_month + "-" + year;
                                // set this date in TextView for Display
                                date_view.setText(date);

                                String date_data = getPlannerData(date);
                                if (date_data != "") {
                                    // get planner data for Date and display it in EditText
                                    //textbx.setText(date_data);
                                    textbx.setText(date_data.substring(date_data.indexOf("_") + 1));
                                    // also set the clock to the time the data specifies
                                    clock.setHour(Integer.parseInt(
                                            date_data.substring(0, date_data.indexOf(":"))));
                                    clock.setMinute(Integer.parseInt(
                                            date_data.substring(date_data.indexOf(":") + 1, date_data.indexOf("_"))));
                                } else {
                                    textbx.setText("");
                                }
                            }
                        });



        // Set Up Calendar_Button listener
        calendar_button
                .setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        //Saves the set time and string in the database
                        editor.putString(date, clock.getHour()
                                + ":"
                                + clock.getMinute()
                                + "_"
                                + textbx.getText().toString());
                        editor.apply();
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

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });


    }

    public void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}