package com.smarteist.imageslider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.imageslider.Model.SliderItem;
import com.yongbeom.aircalendar.AirCalendarDatePickerActivity;

//import net.daum.android.map.MapView;
import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SliderView sliderView;
    private SliderAdapterExample adapter;
    private static TextView office_name_txt, office_loc_txt;
    private static MapView mapView;
    private static ViewGroup mapViewContainer;
    private static int Office_Name_Num;
    private static MapPoint mapPoint;
    private static MapPOIItem marker;
    private Toolbar toolbar;
    ImageView Favorites_btn;

    int i = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_White));

        sliderView = findViewById(R.id.imageSlider);

        adapter = new SliderAdapterExample(this);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        Favorites_btn = findViewById(R.id.Favorites_btn);

        office_name_txt = (TextView) findViewById(R.id.office_name_txt);
        office_loc_txt = (TextView) findViewById(R.id.office_loc_txt);

        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }
        });

        Intent intent = new Intent(this.getIntent());
        Office_Name_Num = intent.getIntExtra("office_Num", 1);

        setUp_Office_name();
        setUp_Office_image();

        Favorites_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1 - i;

                if ( i == 0 ){
                    Favorites_btn.setImageResource(R.drawable.star_on);
                    Toast.makeText(getApplicationContext(), "Added to Favourites", Toast.LENGTH_LONG).show();
                }
                else{
                    Favorites_btn.setImageResource(R.drawable.star_off);
                    Toast.makeText(getApplicationContext(), "Delete to Favourites", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void finish() {
        mapViewContainer.removeView(mapView);
        super.finish();
        getIntent().getExtras().remove("name");
    }

    public void onClick_Book(View v) {        //Map info Activity     //Map Button
        final Intent i = new Intent(this, calendarDate_Book.class);
        startActivityForResult(i, 201);
        //startActivity(i);
    }

    public void setUp_Office_name() {
        String strColor = "#0FD9A2";
        switch (Office_Name_Num) {
            case 1:
                this.setTitle("코워킹스페이스 GARAGE");
                getSupportActionBar().setTitle("코워킹스페이스 강남점");
                office_name_txt.setText("[강남] 코워킹스페이스 GARAGE 강남점" +"\n"+ "- 가라지 강남점 지정데스크");
                office_loc_txt.setTextColor(Color.parseColor(strColor));
                office_loc_txt.setText("서울 강남구 테헤란로 128 2,3층");
                mapPoint = MapPoint.mapPointWithGeoCoord(37.40689362660826, 127.10212894834017);
                mapView.setMapCenterPoint(mapPoint, true);
                mapViewContainer.addView(mapView);

                marker = new MapPOIItem();
                marker.setItemName("판교글로벌R&D센터");
                marker.setTag(0);
                marker.setMapPoint(mapPoint);
                // 기본으로 제공하는 BluePin 마커 모양.
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                mapView.addPOIItem(marker);
                break;

            case 2:
                this.setTitle("비라운지 홍대점");
                getSupportActionBar().setTitle("비라운지 홍대점");
                office_name_txt.setText("[홍대인근] 비라운지 공유 오피스 홍대점 - 1인 사무실");
                office_loc_txt.setTextColor(Color.parseColor(strColor));
                office_loc_txt.setText("서울 양천구 신정중앙로 86 5층");
                mapPoint = MapPoint.mapPointWithGeoCoord(37.52652835684546, 126.86109378702771);
                mapView.setMapCenterPoint(mapPoint, true);
                mapViewContainer.addView(mapView);
                marker = new MapPOIItem();
                marker.setItemName("비라운지공유오피스");
                marker.setTag(0);
                marker.setMapPoint(mapPoint);
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                mapView.addPOIItem(marker);
                break;

            case 3:
                this.setTitle("토즈워크센터 강남");
                getSupportActionBar().setTitle("토즈워크센터 강남2호점");
                office_name_txt.setText("[강남] 토즈워크센터 - 1인 독립형 오피스");
                office_loc_txt.setTextColor(Color.parseColor(strColor));
                office_loc_txt.setText("서울 강남구 역삼동 832-7 황화빌딩 5층");
                mapPoint = MapPoint.mapPointWithGeoCoord(37.40689362660826, 127.10212894834017);
                mapView.setMapCenterPoint(mapPoint, true);
                mapViewContainer.addView(mapView);
                marker = new MapPOIItem();
                marker.setItemName("토즈워크센터");
                marker.setTag(0);
                marker.setMapPoint(mapPoint);
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                mapView.addPOIItem(marker);
                break;

            default:
                System.out.println("Unable to get a office name");
                break;
        }
    }

    public void setUp_Office_image(){
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 3; i++) {
            SliderItem sliderItem = new SliderItem();
            //sliderItem.setDescription("Slider Item " + i);
            switch (Office_Name_Num) {
                case 1:
                    if (i == 0) {
                        sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/97662373-37f56380-1aba-11eb-9a0c-656c07b54f87.jpg");
                    } else if (i == 1){
                        sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/97662375-388dfa00-1aba-11eb-876e-46510abfa96d.jpg");
                    } else if (i == 2){
                        sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/97662406-4b083380-1aba-11eb-917b-d1edf6f94d6e.jpg");
                    }
                    sliderItemList.add(sliderItem);
                    break;

                case 2:
                    if (i == 0) {
                        sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/97950108-26bd9700-1dd9-11eb-966a-eb829a9c8f76.jpg");
                    } else if (i == 1){
                        sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/97950111-291ff100-1dd9-11eb-93a1-ae0a86936e8e.jpg");
                    } else if (i == 2){
                        sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/97950112-29b88780-1dd9-11eb-9408-80402b8a6b33.jpg");
                    }
                    sliderItemList.add(sliderItem);
                    break;

                case 3:
                    if (i == 0) {
                        sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/98208403-9457f780-1f80-11eb-8fcc-3b796c583c49.jpg");
                    } else if (i == 1){
                        sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/98208416-9752e800-1f80-11eb-9f0d-847bbd194edb.jpg");
                    } else if (i == 2){
                        sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/98208421-99b54200-1f80-11eb-9c9d-a976ced83b15.jpg");
                    }
                    sliderItemList.add(sliderItem);
                    break;
            }

        }
        adapter.renewItems(sliderItemList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_navigationmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account:
                Toast.makeText(getApplicationContext(), "navigation menu test", Toast.LENGTH_LONG).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
