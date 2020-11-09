package com.smarteist.imageslider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.yongbeom.aircalendar.AirCalendarDatePickerActivity;
import com.yongbeom.aircalendar.core.AirCalendarIntent;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

import java.util.Calendar;


public class calendarDate_Book extends AppCompatActivity {
    public final static int REQUEST_CODE = 1;

    private static int Office_Name_Num;
    private static TextView result_name,
            result_check_in_time, result_check_out_time;
    private String get_date_Start, get_date_End;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_White));

        AirCalendarIntent intent = new AirCalendarIntent(this);
        intent.setSelectButtonText("Select");
        intent.setResetBtnText("Reset");
        intent.setWeekStart(Calendar.MONDAY);

        result_name = (TextView)findViewById(R.id.result_name);
        result_check_in_time = (TextView)findViewById(R.id.result_check_in_time);
        result_check_out_time = (TextView)findViewById(R.id.result_check_out_time);
//        ArrayList<String> weekDay = new ArrayList<>();
//        weekDay.add("M");
//        weekDay.add("T");
//        weekDay.add("W");
//        weekDay.add("T");
//        weekDay.add("F");
//        weekDay.add("S");
//        weekDay.add("S");
//        intent.setCustomWeekDays(weekDay);
//        intent.setWeekDaysLanguage(AirCalendarIntent.Language.EN);
//        intent.isSingleSelect(false);
//        intent.isBooking(false); // DEFAULT false
//        intent.isSelect(true); // DEFAULT false
//        intent.setBookingDateArray();
//        intent.setStartDate(2017, 12, 1);
//        intent.setEndDate(2017, 12, 1);
//        intent.isMonthLabels(false);
//        intent.setActiveMonth(3);
//        intent.setStartYear(2018);
//        intent.setMaxYear(2030);
        startActivityForResult(intent, REQUEST_CODE);

        Intent result_intent = new Intent(this.getIntent());
        Office_Name_Num = result_intent.getIntExtra("office_Num", 1);
        setUp_Office_name();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        this.setTitle("예약 정보 확인 Activity");

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
//            AirCalendarDatePickerActivity.RESULT_SELECT_START_DATE
//            AirCalendarDatePickerActivity.RESULT_SELECT_END_DATE
//            AirCalendarDatePickerActivity.RESULT_SELECT_START_VIEW_DATE
//            AirCalendarDatePickerActivity.RESULT_SELECT_END_VIEW_DATE
//            AirCalendarDatePickerActivity.RESULT_FLAG
//            AirCalendarDatePickerActivity.RESULT_TYPE
//            AirCalendarDatePickerActivity.RESULT_STATE

            if(data != null){
                Toast.makeText(this, "Select Date range : "
                        + data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_START_DATE) + " ~ "
                        + data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_END_DATE), Toast.LENGTH_SHORT).show();
                get_date_Start = data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_START_DATE);
                get_date_End = data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_END_DATE);

                result_check_in_time.setText(get_date_Start);
                result_check_out_time.setText(get_date_End);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUp_Office_name() {

        switch (Office_Name_Num) {
            case 1:
                result_name.setText("코워킹스페이스 GARAGE 강남점");
                break;

            case 2:
                result_name.setText("비라운지 강남점");

                break;

            case 3:
                result_name.setText("토즈워크센터 강남점");

                break;

            default:
                System.out.println("Unable to get a office name");
                break;
        }
    }

}
