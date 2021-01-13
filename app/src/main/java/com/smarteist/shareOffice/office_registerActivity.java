package com.smarteist.shareOffice;

import android.os.Bundle;

import com.smarteist.imageslider.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class office_registerActivity extends AppCompatActivity {
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_White));
        this.setTitle("토즈워크센터 강남");
        getSupportActionBar().setTitle("제품 등록 글쓰기");

    }


}
