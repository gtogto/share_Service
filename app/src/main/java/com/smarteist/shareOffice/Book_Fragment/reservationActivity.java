package com.smarteist.shareOffice.Book_Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.smarteist.imageslider.R;
import com.smarteist.shareOffice.kakao_Login.kakaoLogin;
import com.smarteist.shareOffice.main_page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class reservationActivity extends AppCompatActivity {
    private ImageView call_btn;
    ImageView ble_btn;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation);

        ble_btn = (ImageView)findViewById(R.id.ble_btn);
        call_btn = (ImageView)findViewById(R.id.call_btn);

        call_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-9277-1592"));
                startActivity(mIntent);

            }
        });

        ble_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1 - i;

                if ( i == 0 ){
                    ble_btn.setImageResource(R.drawable.ic_action_device_access_bluetooth_searching_off);
                    Toast.makeText(getApplicationContext(), "Clicked BLE button off", Toast.LENGTH_LONG).show();
                }
                else{
                    ble_btn.setImageResource(R.drawable.ic_action_device_access_bluetooth_searching);
                    Toast.makeText(getApplicationContext(), "Clicked BLE button on", Toast.LENGTH_LONG).show();
                }
            }
        });

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
                Toast.makeText(getApplicationContext(), "Account menu", Toast.LENGTH_LONG).show();
                return true;

            case R.id.logout:
                Toast.makeText(getApplicationContext(), "Log-out menu", Toast.LENGTH_LONG).show();
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(reservationActivity.this, kakaoLogin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onClick_1st (View v){
        Toast.makeText(getApplicationContext(), "Clicked 1st key button", Toast.LENGTH_LONG).show();
    }

    public void onClick_2nd (View v){
        Toast.makeText(getApplicationContext(), "Clicked Open Door button", Toast.LENGTH_LONG).show();
    }

}
