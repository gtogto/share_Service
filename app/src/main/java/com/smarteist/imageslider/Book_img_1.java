package com.smarteist.imageslider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yongbeom.aircalendar.AirCalendarDatePickerActivity;
import com.yongbeom.aircalendar.core.AirCalendarIntent;

import androidx.appcompat.app.AppCompatActivity;

public class Book_img_1 extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_layout);
        /*
        AirCalendarIntent intent = new AirCalendarIntent(this);
            intent.isBooking(false);
            intent.isSelect(false);
            intent.setBookingDateArray('Array Dates( format: yyyy-MM-dd');
            intent.setStartDate(yyyy , MM , dd); // int
            intent.setEndDate(yyyy , MM , dd); // int
            intent.isMonthLabels(false);
            intent.setSelectButtonText("Select"); //the select button text
            intent.setResetBtnText("Reset"); //the reset button text
            intent.setWeekStart(Calendar.MONDAY);
            intent.setWeekDaysLanguage(AirCalendarIntent.Language.EN); //language for the weekdays

            ArrayList<String> weekDay = new ArrayList<>();
            weekDay.add("M");
            weekDay.add("T");
            weekDay.add("W");
            weekDay.add("T");
            weekDay.add("F");
            weekDay.add("S");
            weekDay.add("S");
            intent.setCustomWeekDays(weekDay); //custom weekdays

            startActivityForResult(intent, REQUEST_CODE);*/

    }

    /*
    public void onClick_ok(View v) {        //Map info Activity     //Map Button
        final Intent i = new Intent(this, Book_img_2.class);
        startActivityForResult(i, 201);
        //startActivity(i);
    }*/
}
