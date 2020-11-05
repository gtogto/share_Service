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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class main_page extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    //SliderView sliderView;
    //private SliderAdapterExample adapter;

    private TextView text_item_1, text_item_2, text_item_3, text_item_4, text_item_5, text_item_6, text_item_7, text_item_8, text_item_9;
    private TextView price_item_1, price_item_2, price_item_3, price_item_4, price_item_5, price_item_6, price_item_7, price_item_8, price_item_9;
    //android:id="@+id/text_item_1"

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_layout);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_White));
        getSupportActionBar().setTitle("Office Share");

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

        price_item_1 = (TextView)findViewById(R.id.price_item_1); price_item_2 = (TextView)findViewById(R.id.price_item_2);
        price_item_3 = (TextView)findViewById(R.id.price_item_3); price_item_4 = (TextView)findViewById(R.id.price_item_4);
        price_item_5 = (TextView)findViewById(R.id.price_item_5); price_item_6 = (TextView)findViewById(R.id.price_item_6);
        price_item_7 = (TextView)findViewById(R.id.price_item_7); price_item_8 = (TextView)findViewById(R.id.price_item_8);
        price_item_9 = (TextView)findViewById(R.id.price_item_9);

        //getAppKeyHash();
        setOfficeText();

    }

    public void onClick_img1(View v) {        //Map info Activity     //Map Button
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("office_Num",1);
        startActivity(intent);
    }

    public void onClick_img2(View v) {        //Map info Activity     //Map Button
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("office_Num",2);
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

    public void setOfficeText(){
        /* Office information list */
        String strColor = "#6978F8";
        text_item_1.setText(getString(R.string.office_name_item1));
        price_item_1.setTextColor(Color.parseColor(strColor));
        price_item_1.setText("10,000sys/h");

        text_item_2.setText(getString(R.string.office_name_item2));
        price_item_2.setTextColor(Color.parseColor(strColor));
        price_item_2.setText("10,000sys/h");

        text_item_3.setText(getString(R.string.office_name_item3));
        price_item_3.setTextColor(Color.parseColor(strColor));
        price_item_3.setText("10,000sys/h");

        text_item_4.setText(getString(R.string.office_name_item4));
        price_item_4.setTextColor(Color.parseColor(strColor));
        price_item_4.setText("10,000sys/h");

        text_item_5.setText(getString(R.string.office_name_item5));
        price_item_5.setTextColor(Color.parseColor(strColor));
        price_item_5.setText("10,000sys/h");

        text_item_6.setText(getString(R.string.office_name_item6));
        price_item_6.setTextColor(Color.parseColor(strColor));
        price_item_6.setText("10,000sys/h");

        text_item_7.setText(getString(R.string.office_name_item7));
        price_item_7.setTextColor(Color.parseColor(strColor));
        price_item_7.setText("10,000sys/h");

        text_item_8.setText(getString(R.string.office_name_item8));
        price_item_8.setTextColor(Color.parseColor(strColor));
        price_item_8.setText("10,000sys/h");

        text_item_9.setText(getString(R.string.office_name_item9));
        price_item_9.setTextColor(Color.parseColor(strColor));
        price_item_9.setText("10,000sys/h");
    }

}
