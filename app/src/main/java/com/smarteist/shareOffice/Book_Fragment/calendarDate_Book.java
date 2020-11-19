package com.smarteist.shareOffice.Book_Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.smarteist.imageslider.R;
import com.yongbeom.aircalendar.AirCalendarDatePickerActivity;
import com.yongbeom.aircalendar.core.AirCalendarIntent;

import java.util.Calendar;

import static com.smarteist.shareOffice.main_page.get_user_Name;


public class calendarDate_Book extends AppCompatActivity {
    public final static int REQUEST_CODE = 1;

    book_Fragment_1 book_fragment_1;
    book_Fragment_2 book_fragment_2;

    private static int Office_Name_Num;
    private static TextView result_name,
            result_check_in_time, result_check_out_time, get_user_name;
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
        get_user_name = (TextView)findViewById(R.id.test2);

        book_fragment_1 = new book_Fragment_1();
        book_fragment_2 = new book_Fragment_2();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, book_fragment_1).commit();

        TabLayout tabs = (TabLayout)findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("예약 정보"));
        tabs.addTab(tabs.newTab().setText("지도 보기"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            //탭선택시
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("MainActivity", "선택된 탭 : "+position);

                Fragment selected = null;
                if(position==0){
                    selected = book_fragment_1;
                }else if(position==1){
                    selected = book_fragment_2;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }
            //탭선택해제시
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            //선탭된탭을 다시 클릭시
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



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
                get_date_Start = data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_START_VIEW_DATE);
                get_date_End = data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_END_VIEW_DATE);

                result_check_in_time.setText(get_date_Start);
                result_check_out_time.setText(get_date_End);
                System.out.println("Get USER NAME : " + get_user_Name);     // get user KAKAO name
                get_user_name.setText(get_user_Name);
                /*
                System.out.println("RESULT_SELECT_START_DATE = " + data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_START_DATE));
                System.out.println("RESULT_SELECT_END_DATE = " + data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_END_DATE));
                System.out.println("RESULT_SELECT_START_VIEW_DATE = " + data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_START_VIEW_DATE));
                System.out.println("RESULT_SELECT_END_VIEW_DATE = " + data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_END_VIEW_DATE));
                System.out.println("RESULT_FLAG = " + data.getStringExtra(AirCalendarDatePickerActivity.RESULT_FLAG));
                System.out.println("RESULT_TYPE = " + data.getStringExtra(AirCalendarDatePickerActivity.RESULT_TYPE));
                System.out.println("RESULT_STATE = " + data.getStringExtra(AirCalendarDatePickerActivity.RESULT_STATE));
                 */

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
