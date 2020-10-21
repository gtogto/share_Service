package com.smarteist.imageslider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Book_img_1 extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_layout);
    }

    public void onClick_ok(View v) {        //Map info Activity     //Map Button
        final Intent i = new Intent(this, Book_img_2.class);
        startActivityForResult(i, 201);
        //startActivity(i);
    }
}
