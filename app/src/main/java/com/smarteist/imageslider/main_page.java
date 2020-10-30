package com.smarteist.imageslider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.imageslider.Model.SliderItem;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class main_page extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    //SliderView sliderView;
    //private SliderAdapterExample adapter;

    private static TextView text_item_1, text_item_2, text_item_3, text_item_4, text_item_5, text_item_6, text_item_7, text_item_8, text_item_9;
    //android:id="@+id/text_item_1"

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_layout);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*
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
        sliderView.startAutoCycle();*/

        text_item_1 = (TextView)findViewById(R.id.text_item_1); text_item_2 = (TextView)findViewById(R.id.text_item_2);
        text_item_3 = (TextView)findViewById(R.id.text_item_3); text_item_4 = (TextView)findViewById(R.id.text_item_4);
        text_item_5 = (TextView)findViewById(R.id.text_item_5); text_item_6 = (TextView)findViewById(R.id.text_item_6);
        text_item_7 = (TextView)findViewById(R.id.text_item_7); text_item_8 = (TextView)findViewById(R.id.text_item_8);
        text_item_9 = (TextView)findViewById(R.id.text_item_9);

        text_item_1.setTextSize(15);
        text_item_1.setText("[압구정] 우노빌딩 10,000sys/h");

        text_item_2.setTextSize(15);
        text_item_2.setText("[홍대] 회의실 10,000sys/h");

        text_item_3.setTextSize(15);
        text_item_3.setText("[논현역] 공유오피스 8,000sys/h");

        text_item_4.setTextSize(15);
        text_item_4.setText("[판교역] 우노빌딩 10,000sys/h");

        text_item_5.setTextSize(15);
        text_item_5.setText("[압구정] 우노빌딩 10,000sys/h");

        text_item_6.setTextSize(15);
        text_item_6.setText("[압구정] 우노빌딩 10,000sys/h");

        text_item_7.setTextSize(15);
        text_item_7.setText("[압구정] 우노빌딩 10,000sys/h");

        text_item_8.setTextSize(15);
        text_item_8.setText("[압구정] 우노빌딩 10,000sys/h");

        text_item_9.setTextSize(15);
        text_item_9.setText("[압구정] 우노빌딩 10,000sys/h");

        /*
        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }
        });
        */
        //test();
        getAppKeyHash();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_navigationmenu, menu);
        return true;
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }
    /*
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                Toast.makeText(this, "New Member", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 뒤로가기 버튼 눌렀을 때
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    public void test(){
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 3; i++) {
            SliderItem sliderItem = new SliderItem();
            //sliderItem.setDescription("Slider Item " + i);
            if (i == 0) {
                sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/97400217-0fdaf880-1932-11eb-8c52-e1ae418417e5.png");
            } else if (i == 1){
                sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/92438408-b8e35d80-f1e3-11ea-889f-c5c0649ff4d8.png");
            } else if (i == 2){
                sliderItem.setImageUrl("https://user-images.githubusercontent.com/30851459/92438528-f8aa4500-f1e3-11ea-9294-09846273c9dc.png");
            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }
    */

    public void onClick_img1(View v) {        //Map info Activity     //Map Button
        final Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, 201);
        intent.putExtra("name", "[강남] 코워킹스페이스 GARAGE 강남점 - 가라지 강남점 지정데스크");
        startActivity(intent);
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

}
