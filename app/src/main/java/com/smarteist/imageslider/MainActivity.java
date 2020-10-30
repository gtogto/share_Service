package com.smarteist.imageslider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.smarteist.autoimageslider.IndicatorView.PageIndicatorView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.imageslider.Model.SliderItem;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

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

        office_name_txt = (TextView) findViewById(R.id.office_name_txt);
        office_loc_txt = (TextView) findViewById(R.id.office_loc_txt);

        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(37.40689362660826, 127.10212894834017);
        mapView.setMapCenterPoint(mapPoint, true);
        mapViewContainer.addView(mapView);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("판교글로벌R&D센터");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker);

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }
        });

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name"); /*String형*/
        office_name_txt.setText(name);

        test();

    }

    @Override
    public void finish() {
        mapViewContainer.removeView(mapView);
        super.finish();
    }

    public void test(){
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 3; i++) {
            SliderItem sliderItem = new SliderItem();
            //sliderItem.setDescription("Slider Item " + i);
            if (i == 0) {
                sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/97662373-37f56380-1aba-11eb-9a0c-656c07b54f87.jpg");
            } else if (i == 1){
                sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/97662375-388dfa00-1aba-11eb-876e-46510abfa96d.jpg");
            } else if (i == 2){
                sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/97662406-4b083380-1aba-11eb-917b-d1edf6f94d6e.jpg");
            }

            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

    public void renewItems(View view) {
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        /* Slider image*/
        for (int i = 0; i < 3; i++) {
            SliderItem sliderItem = new SliderItem();
            //sliderItem.setDescription("Office Image " + i);
            if (i == 0) {
                sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/92436005-9b5fc500-f1de-11ea-90de-5472c738a66c.png");
            } else if (i == 1){
                sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/92436009-9e5ab580-f1de-11ea-8c04-ff1367463b54.png");
            } else if (i == 2) {
                sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/92436018-a286d300-f1de-11ea-8046-edabe48ed581.png");
            }

            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }
    /*
    public void removeLastItem(View view) {
        if (adapter.getCount() - 1 >= 0)
            adapter.deleteItem(adapter.getCount() - 1);
    }

    public void addNewItem(View view) {
        SliderItem sliderItem = new SliderItem();
        sliderItem.setDescription("Slider Item Added Manually");
        sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        adapter.addItem(sliderItem);
    }*/

    public void onClick_Book(View v) {        //Map info Activity     //Map Button
        final Intent i = new Intent(this, Book_img_1.class);
        startActivityForResult(i, 201);
        //startActivity(i);
    }


}
