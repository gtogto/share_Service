package com.smarteist.shareOffice.Book_Fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.smarteist.imageslider.R;
import com.smarteist.shareOffice.BLE.BluetoothLeService;
import com.smarteist.shareOffice.BLE.DeviceScanActivity;
import com.smarteist.shareOffice.BLE.SampleGattAttributes;
import com.smarteist.shareOffice.WasService;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.smarteist.shareOffice.BLE.BluetoothLeService.EXTRA_DATA;

public class reservationActivity extends AppCompatActivity {
    private final static String TAG = "DCA";

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    //TODO: BLE variable
    private TextView mConnectionState;
    private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss");
    public static final int DUMP = -1;

    public String mDeviceName;
    public String mDeviceAddress;
    public static BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    public static ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

    public static byte[] packet;
    public static byte[] send_packet;
    public static String action;

    /* wdb Server */
    //public static final String sIP = "118.221.46.185";  // dApp Server ip address
    //public static final int sPORT = 5000;
    public static final String sIP = "192.168.0.10";  // dApp Server ip address #2020.12.03
    public static final int sPORT = 8081;

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

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            System.out.println("gto -> onServiceConnected ");
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                System.out.println("gto -> unable bluetooth ");
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
            System.out.println("main service disconnect");
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            System.out.println("main Function Test action = " + action );
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                System.out.println("main Function Test 1");
                System.out.println("main Function Test 1 action = " + action );
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                System.out.println("main Function Test 2");
                System.out.println("main Function Test 2 action = " + action );

                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                System.out.println("main Function Test 3");
                System.out.println("main Function Test 3 action = " + action );

                final BluetoothGattCharacteristic notifyCharacteristic = getNottifyCharacteristic();
                if (notifyCharacteristic == null) {
                    Toast.makeText(getApplication(), "gatt_services can not supported", Toast.LENGTH_SHORT).show();
                    System.out.println("main Function Test 4");
                    System.out.println("main Function Test 4 action = " + action );

                    mConnected = false;
                    return;
                }
                final int charaProp = notifyCharacteristic.getProperties();
                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    mBluetoothLeService.setCharacteristicNotification(
                            notifyCharacteristic, true);
                    System.out.println("main Function Test 5");
                    System.out.println("main Function Test 5 action = " + action );

                }

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                packet = intent.getByteArrayExtra(EXTRA_DATA);
                displayData(packet);
                System.out.println("main Function Test 6");
                System.out.println("main Function Test 6 action = " + action );

            }
        }
    };

    // 헥사 접두사 "0x" 붙이는 버전
    public static String stringToHex0x(String s) {
        String result = "";

        for (int i = 0; i < s.length(); i++) {
            result += String.format("0x%02X ", (int) s.charAt(i));
        }

        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation);

        System.out.println("mBluetoothLeService value main = " + mBluetoothLeService) ;

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        try {
            socket = new DatagramSocket(5000);
            serverAddr = InetAddress.getByName(sIP);
            System.out.println("gto -> try");
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("gto -> SocketException");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("gto -> UnknownHostException");
        }

        mConnectionState = (TextView) findViewById(R.id.connection_state);

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

                ble_btn.setImageResource(R.drawable.ic_action_device_access_bluetooth_searching_off);
                Toast.makeText(getApplicationContext(), "Clicked BLE button off", Toast.LENGTH_LONG).show();
                Intent intent_1 = new Intent(getApplicationContext(), DeviceScanActivity.class);
                startActivityForResult(intent_1, 201);
                /*
                i = 1 - i;

                if ( i == 0 ){
                    ble_btn.setImageResource(R.drawable.ic_action_device_access_bluetooth_searching_off);
                    Toast.makeText(getApplicationContext(), "Clicked BLE button off", Toast.LENGTH_LONG).show();
                    Intent intent_1 = new Intent(getApplicationContext(), DeviceScanActivity.class);
                    startActivityForResult(intent_1, 201);
                }
                else{
                    ble_btn.setImageResource(R.drawable.ic_action_device_access_bluetooth_searching);
                    Toast.makeText(getApplicationContext(), "Clicked BLE button on", Toast.LENGTH_LONG).show();
                }*/
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
                    mSendWasData = new SendWasData(50010001, 1);
                    mSendWasData.action();
                }
            }
        };
        timer =  new Timer();

        mFirst_PW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked 1st key button", Toast.LENGTH_LONG).show();
                final String server_url = "http://192.168.0.10:8081";
                mSendWasData = new SendWasData(50010001, 1);
                mSendWasData.action();
                timer.schedule(tt,0,2000);
                System.out.println("gto -> SendWasData");
            }
        });

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

    }

    class SendWasData{
        private int deviceNo;
        private int st;

        //final String server_url = "http://125.131.9.88:8082";
        final String server_url = "http://192.168.0.10:8081";

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
                        System.out.println("gto -> instantPassword = " + instantPassword);
                        System.out.println("gto -> instantPassword = " + response.body());
                        String st = response.body().get("st").getAsString();
                        System.out.println("gto -> st = " + st);
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
                        else {
                            receiveWasData = instantPassword.split("");
                            //String pType = receiveWasData[1];
                            //String pSt = receiveWasData[2];
                            //String pDeviceNo = receiveWasData[3];
                            System.out.println("gto -> instantPassword not 8byte ==> " + receiveWasData);
                            //System.out.println("gto -> json ascii To hex : " + hexToASCII(instantPassword));
                            StringBuilder builder = new StringBuilder();
                            for(String s : receiveWasData) {
                                builder.append(s);
                            }
                            String str = builder.toString();
                            System.out.println("gto -> json length : " + instantPassword.length());
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

    //TODO BLE Packet receive
    protected void onResume() {
        super.onResume();
        System.out.println("run on Resume function");
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "setup Connect request result=" + result + "*********************");

        }
        else {
            Log.d(TAG, "setup Connect request result= " + mBluetoothLeService);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    protected void onDestroy() {
        super.onDestroy();
        //unbindService(mServiceConnection);
        mBluetoothLeService = null;
        Log.d(TAG, "setup onDestroy request");
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }

    public void displayData(byte[] packet) {

        if (packet != null) {

            System.out.println("Enable packet");
        }

    }

    private String getStringPacket(byte[] packet) {

        String hex_value = "";

        for (byte b : packet) {  						//readBuf -> Hex
            hex_value += Integer.toString((b & 0xF0) >> 4, 16);
            hex_value += Integer.toString(b & 0x0F, 16);
        }

        System.out.println("By. SetUp activity : "+ hex_value);
        StringBuilder sb = new StringBuilder(packet.length * 2);

        System.out.println("By. SetUp HEX To ASCII : "+ hexToASCII(hex_value));

        Toast tMsg = Toast.makeText(reservationActivity.this, hexToASCII(hex_value), Toast.LENGTH_SHORT);
        tMsg.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout view = (LinearLayout) tMsg.getView();
        tMsg.show();

        return sb.toString();
    }

    private BluetoothGattCharacteristic getNottifyCharacteristic(){

        BluetoothGattCharacteristic notifyCharacteristic = null;
        if(mGattCharacteristics == null || mGattCharacteristics.size() == 0){
            return null;
        }
        for (int i = 0; i < mGattCharacteristics.size() ; i++) {
            for (int j = 0; j < mGattCharacteristics.get(i).size() ; j++) {
                notifyCharacteristic =  mGattCharacteristics.get(i).get(j);
                if(notifyCharacteristic.getUuid().equals(BluetoothLeService.FFF4_RATE_MEASUREMENT)){
                    return notifyCharacteristic;
                }
                else if(notifyCharacteristic.getUuid().equals(BluetoothLeService.JDY_TX_MEASUREMENT)){
                    return notifyCharacteristic;
                }
            }
        }
        return null;
    }


    private BluetoothGattCharacteristic getWriteGattCharacteristic(){

        BluetoothGattCharacteristic writeGattCharacteristic = null;
        if(mGattCharacteristics == null || mGattCharacteristics.size() == 0){
            System.out.println("run getWriteGattCharacteristic null");
            return null;
        }

        for (int i = 0; i < mGattCharacteristics.size() ; i++) {
            for (int j = 0; j < mGattCharacteristics.get(i).size() ; j++) {
                writeGattCharacteristic =  mGattCharacteristics.get(i).get(j);
                if(writeGattCharacteristic. getUuid().equals(BluetoothLeService.FFF3_RATE_MEASUREMENT)){
                    return writeGattCharacteristic;
                }

                else if(writeGattCharacteristic. getUuid().equals(BluetoothLeService.JDY_RX_MEASUREMENT)){
                    return writeGattCharacteristic;
                }
            }
        }
        System.out.println("setup getWriteGattCharacteristic for");
        return null;
    }

    private void displayGattServices(List<BluetoothGattService> gattServices) {

        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        //System.out.println("run device NAME = " + LIST_NAME);
        System.out.println("run device UUID = " + uuid);
    }



    // UDP
    //데이터 보내는 쓰레드 클래스
    public static class SendData extends Thread {

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

    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bluetooth_chat, menu);
    }

    private static String hexToASCII(String hexValue)
    {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexValue.length(); i += 2)
        {
            String str = hexValue.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    private static IntentFilter makeGattUpdateIntentFilter() {

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_SEND_PACKET);
        return intentFilter;
    }

}
