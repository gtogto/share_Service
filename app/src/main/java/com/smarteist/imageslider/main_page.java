package com.smarteist.imageslider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.smarteist.autoimageslider.SliderView;
import androidx.appcompat.app.AppCompatActivity;
import com.smarteist.autoimageslider.IndicatorView.PageIndicatorView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.imageslider.Model.SliderItem;

import java.util.ArrayList;
import java.util.List;

public class main_page extends AppCompatActivity {

    SliderView sliderView;
    private SliderAdapterExample adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }
        });

        test();
    }

    public void test(){
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 3; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDescription("Slider Item " + i);
            if (i == 0) {
                sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/92438400-b4b74000-f1e3-11ea-9e94-2f214e861572.png");
            } else if (i == 1){
                sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/92438408-b8e35d80-f1e3-11ea-889f-c5c0649ff4d8.png");
            } else if (i == 2){
                sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/92438528-f8aa4500-f1e3-11ea-9294-09846273c9dc.png");
            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

    public void onClick_img1(View v) {        //Map info Activity     //Map Button
        final Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, 201);
        //startActivity(i);
    }

}
