package com.smarteist.shareOffice.Book_Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.smarteist.imageslider.R;
import com.smarteist.shareOffice.WasService;
import com.smarteist.shareOffice.kakao_Login.kakaoLogin;
import com.smarteist.shareOffice.main_page;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class reservationActivity extends AppCompatActivity {
    /* wdb Server */
    public static final String sIP = "118.221.46.185";  // dApp Server ip address
    public static final int sPORT = 5000;

    // SERVER Communication - kuloeh
    public static final String wasIP ="125.131.9.88";   // dApp Server ip address
    public static final int wasPORT = 8082;

    // class for data send
    public SendData mSendData = null;
    public ReceiveData mReceiveData = null;

    static byte[] sendPacket = new byte[32];

    //UDP 통신용 소켓 생성
    static DatagramSocket socket;
    //서버 주소 변수
    static InetAddress serverAddr;

    // Timer
    private boolean tFlag=false;
    // retrofit
    public SendWasData mSendWasData =null;
    static String[] receiveWasData = new String[8];
    //BC _No
    private int bc_no = 1;
    private TimerTask tt = null;
    private Timer timer = null;
    //bc editText

    private ImageView call_btn;

    private Button mFirst_PW;
    private Button mSecond_PW;
    private TextView tPairingAuthStatus;
    private TextView tFirst_PW;
    private TextView tSecond_PW;
    ImageView ble_btn;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation);

        try {
            socket = new DatagramSocket(5000);
            serverAddr = InetAddress.getByName(sIP);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        ble_btn = (ImageView)findViewById(R.id.ble_btn);
        call_btn = (ImageView)findViewById(R.id.call_btn);

        mFirst_PW = (Button)findViewById(R.id.mfirst_PW);
        mSecond_PW = (Button)findViewById(R.id.mseond_PW);

        tFirst_PW = (TextView)findViewById(R.id.first_PW);
        tSecond_PW = (TextView)findViewById(R.id.second_PW);

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

        mReceiveData = new ReceiveData();
        mReceiveData.start();

        // Timer
        tt = new TimerTask() {
            @Override
            public void run() {
                if(tFlag){
                    //2차패스워드
                    mSendWasData = new SendWasData(50010000+bc_no, 2);
                    mSendWasData.action();
                }
            }
        };
        timer =  new Timer();

        tFirst_PW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked 1st key button", Toast.LENGTH_LONG).show();
                final String server_url = "http://125.131.9.88:8082";
                mSendWasData = new SendWasData(50010000+bc_no, 1);
                mSendWasData.action();
                timer.schedule(tt,0,2000);
            }
        });

    }

    class SendWasData{
        private int deviceNo;
        private int st;

        final String server_url = "http://125.131.9.88:8082";

        public SendWasData(int argDeviceNo, int argSt){
            this.deviceNo = argDeviceNo;
            this.st = argSt;
        }

        public void action() {
            // Retrofit 객체생성 -kuloeh
            Retrofit retrofit = new Retrofit.Builder()
                    //                //서버 url설정
                    .baseUrl(server_url)
                    //                //데이터 파싱 설정
                    .addConverterFactory(GsonConverterFactory.create())
                    //                //객체정보 반환
                    .build();
            WasService service = retrofit.create(WasService.class);
            Call<JsonObject> request = service.getWasData(deviceNo, st);

            request.enqueue(new Callback<JsonObject>() {        //request.enqueue(new Callback<JsonObject>
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        String instantPassword = response.body().get("instantPassword").getAsString();
                        String st = response.body().get("st").getAsString();
                        if (instantPassword.length() == 8) {
                            receiveWasData = instantPassword.split("");
                            String pType = receiveWasData[1];
                            String pSt = receiveWasData[2];
                            String pDeviceNo = receiveWasData[3];

                            if(receiveWasData[2].equals("1")){
                                tFirst_PW.setTextColor(Color.parseColor(("red")));
                                tFirst_PW.setText(instantPassword.toCharArray(), 3, 5);
                                tFlag = true;
                            }else{
                                tFirst_PW.setTextColor(Color.parseColor(("blue")));
                                tFirst_PW.setText("Verified Near Door");
                                tSecond_PW.setTextColor(Color.parseColor(("blue")));
                                tSecond_PW.setText("["+instantPassword+"]\n코드를 입력해주세요.");
                                tFlag = false;
                                tt.cancel();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    tFirst_PW.setTextColor(Color.parseColor(("black")));
                    tFirst_PW.setText("Pairing Code");
                    tFlag = false;
                }
            });
        }
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

    // UDP
    //데이터 보내는 쓰레드 클래스
    static class SendData extends Thread {

        private byte destId = 0x30;
        private byte sourceId = 0x30;
        private byte serviceCode = 0x30;

        public SendData(byte argDestId, byte argSourceId, byte argServiceCode){
            this.destId = argDestId;
            this.sourceId = argSourceId;
            this.serviceCode = argServiceCode;
        }

        public void run() {
            try {

                // Creation send message
                //byte[] buf = ("H").getBytes();
                for(int i=0; i<32; i++){
                    sendPacket[i] = 0x00;
                }
                sendPacket[0] = 'S';
                sendPacket[1] = this.destId;
                sendPacket[2] = this.sourceId;
                sendPacket[3] = this.serviceCode;
                sendPacket[31] = 'E';

                // Convert packet
                DatagramPacket packet = new DatagramPacket(sendPacket, sendPacket.length, serverAddr, sPORT);

                socket.send(packet);

            } catch (Exception e) {

            }
        }
    }


    // UDP
    //데이터 수신 쓰레드 클래스
    class ReceiveData extends Thread {

        byte[] receivePacket = new byte[32];

        public void run() {
            try {
                //UDP 통신용 소켓 생성
                DatagramPacket recvPacket = new DatagramPacket(this.receivePacket, this.receivePacket.length);

                while(true) {
                    // Waiting receive message
                    socket.receive(recvPacket);

                    // Convert message type (byte[]) to String.
                    String msg = new String(recvPacket.getData());
                    // Log.i(TAG, msg);

                    // [2] 1's Auth
                    if(recvPacket.getData()[3] == (byte) 0xA0){
                        tFirst_PW.setText(msg.toCharArray(), 4, 6);
                    } else if(recvPacket.getData()[3] == (byte) 0xA1) {
                        tSecond_PW.setText(msg.toCharArray(), 4, 6);
                    } else if(recvPacket.getData()[3] == (byte) 0xAB) {
                        tPairingAuthStatus.setText("Pairing Auto: Yes");
                    }
                }

            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    /*
    public void onClick_1st (View v){
        Toast.makeText(getApplicationContext(), "Clicked 1st key button", Toast.LENGTH_LONG).show();
        final String server_url = "http://125.131.9.88:8082";
        mSendWasData = new SendWasData(50010000+bc_no, 1);
        mSendWasData.action();
        timer.schedule(tt,0,2000);
    }

    public void onClick_2nd (View v){
        Toast.makeText(getApplicationContext(), "Clicked Open Door button", Toast.LENGTH_LONG).show();
    }*/

}
