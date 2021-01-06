package com.smarteist.shareOffice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.smarteist.imageslider.R;
import com.smarteist.shareOffice.kakao_Login.kakaoLogin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import static com.kakao.usermgmt.StringSet.nickname;

public class main_page extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Context context = this;

    private TextView text_item_1, text_item_2, text_item_3, text_item_4, text_item_5, text_item_6, text_item_7, text_item_8, text_item_9;
    private TextView price_item_1, price_item_2, price_item_3, price_item_4, price_item_5, price_item_6, price_item_7, price_item_8, price_item_9;
    public static String get_user_Name;

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

        Intent intent = new Intent(this.getIntent());
        get_user_Name = intent.getExtras().getString("name");   // get user KAKAO name

        Log.e("main Profile 1 : ", get_user_Name + "");

        //Log.e("main Profile 2 : ", nickname + "");

        setOfficeText();    // Office information setup

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

    public void onClick_img3(View v) {        //Map info Activity     //Map Button
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("office_Num",3);
        startActivity(intent);
    }

    public void onClick_img4(View v) {        //Map info Activity     //Map Button
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("office_Num",4);
        startActivity(intent);
    }

    public void onClick_img5(View v) {        //Map info Activity     //Map Button
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("office_Num",5);
        startActivity(intent);
    }

    public void onClick_img6(View v) {        //Map info Activity     //Map Button
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("office_Num",6);
        startActivity(intent);
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
                Toast.makeText(getApplicationContext(), "Account menu", Toast.LENGTH_LONG).show();
                return true;
            case R.id.register:
                final Intent intent = new Intent(this, office_registerActivity.class);
                Toast.makeText(getApplicationContext(), "제품 등록 액티비티", Toast.LENGTH_LONG).show();
                startActivity(intent);
                return true;

            case R.id.logout:
                Toast.makeText(getApplicationContext(), "Log-out menu", Toast.LENGTH_LONG).show();
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(main_page.this, kakaoLogin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
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

    /* Generate a Hash Key */
    /*
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
    }*/

}
