package com.smarteist.shareOffice.Book_Fragment;

import android.os.Bundle;
import com.smarteist.imageslider.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class reservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_White));
        getSupportActionBar().setTitle("Secure Access Lock");
        setContentView(R.layout.reservation);
    }

}
