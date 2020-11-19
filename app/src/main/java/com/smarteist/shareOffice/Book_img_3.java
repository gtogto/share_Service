package com.smarteist.shareOffice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.smarteist.imageslider.R;

import androidx.appcompat.app.AppCompatActivity;

public class Book_img_3 extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_reserve_2);
    }

    public void onClick_ok(View v) {        //Map info Activity     //Map Button
        final Intent i = new Intent(this, Book_img_4.class);
        startActivityForResult(i, 201);
        //startActivity(i);
    }
}
