/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smarteist.shareOffice.BLE;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.smarteist.shareOffice.common.CommonData;

import java.util.List;
import java.util.UUID;

public class BluetoothLeService extends Service {
    private final static String TAG = "BLS";

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public final static String ACTION_SEND_PACKET =
            "com.example.bluetooth.le.ACTION_SEND_PACKET";
    public final static String SEND_PACKET =
            "com.example.bluetooth.le.SEND_PACKET";

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);

    public final static UUID FFF4_RATE_MEASUREMENT =
            UUID.fromString(SampleGattAttributes.FFF4_RATE_MEASUREMENT);

    public final static UUID FFF3_RATE_MEASUREMENT =
            UUID.fromString(SampleGattAttributes.FFF3_RATE_MEASUREMENT);

    public final static UUID JDY_TX_MEASUREMENT =
            UUID.fromString(SampleGattAttributes.JDY_TX_MEASUREMENT);

    public final static UUID JDY_RX_MEASUREMENT =
            UUID.fromString(SampleGattAttributes.JDY_RX_MEASUREMENT);

    public final static UUID JDY_RX_MEASUREMENT1 =
            UUID.fromString(SampleGattAttributes.JDY_RX_MEASUREMENT1);

    public int mManboSendCnt;

    public int mPPGSendCnt;

    public int mSleepSendCnt;
    public int mExerciseHRMSendCnt;
    public int mExerciseAltitudeSendCnt;

    public int mCustomSendCnt;

    public int mOTASendCnt;
    public int mOTASendPageCnt;

    BluetoothGattCharacteristic mCharacteristic;

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                Toast.makeText(getApplication(), "Disconnected from GATT server.", Toast.LENGTH_SHORT).show();
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.w(TAG, "mBluetoothGatt = " + mBluetoothGatt );
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG,"onCharacteristicRead : " + characteristic.getValue());

                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            //Log.d(TAG,"onCharacteristicChanged : " + characteristic.getValue());
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        //LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        if (FFF4_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
           // Log.w(TAG, String.format("FFF4_RATE_MEASUREMENT"));

            intent.putExtra(EXTRA_DATA, characteristic.getValue());
           // LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

        if (JDY_TX_MEASUREMENT.equals(characteristic.getUuid())) {
            Log.w(TAG, String.format("RECEIVE DATA BY JDY"));      // Receive data

            intent.putExtra(EXTRA_DATA, characteristic.getValue());
            // LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

        else if (TX_CHAR_UUID.equals(characteristic.getUuid())) {

             Log.w(TAG, String.format("Received TX:" ));
            intent.putExtra(EXTRA_DATA, characteristic.getValue());
        /*} else  if (HEALTH_DATA_UUID.equals(characteristic.getUuid())){
            intent.putExtra(EXTRA_DATA, characteristic.getValue());
*/
        }
 //       LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */

    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */

    public boolean connect(final String address) {

        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        else {
            Log.w(TAG, "BluetoothAdapter initialized or specified address.");
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }



        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;

            //ine
        //mBluetoothGatt.requestConnectionPriority(mBluetoothGatt.CONNECTION_PRIORITY_LOW_POWER);

        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */

    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {

            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }

        if (FFF4_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }

        if (JDY_TX_MEASUREMENT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            Log.w(TAG, String.format("JDY READY"));
            mBluetoothGatt.writeDescriptor(descriptor);
        }

        if (RX_CHAR_UUID.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }

        if (TouchMe_DATA_UUID.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
            Log.w(TAG, String.format("TouchMe Data READY"));

        }

        if (RX_SERVICE_UUID.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }

        if (TouchMe_DATA_UUID.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
            Log.w(TAG, String.format("TouchMe Data READY"));
        }//TouchMe_DATA_UUID

        if (CCCD.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
       /* if (HEALTH_DATA_UUID.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
*/

    }
    public int mState = UART_PROFILE_DISCONNECTED;

    public static final int REQUEST_SELECT_DEVICE = 1;
    public static final int REQUEST_ENABLE_BT = 2;
    public static final int UART_PROFILE_READY = 10;
    public static final int UART_PROFILE_CONNECTED = 20;
    public static final int UART_PROFILE_DISCONNECTED = 21;
    public static final int STATE_OFF = 10;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION_CONTACTS = 0;


    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */

    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }

    public boolean writeGattCharacteristic(BluetoothGattCharacteristic characteristic, int type) {

        byte[] bSendData;
        switch (type) {
            // TODO: URBAN
            case CommonData.URBAN_INFO_CFM: {
                byte[] pData = new byte[1];
                pData[0] = 0x00;
                bSendData = CommonData.SendData(CommonData.URBAN_INFO_CFM, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.URBAN_INFO_DB_CFM: {
                byte[] pData = new byte[1];
                pData[0] = 0x00;
                bSendData = CommonData.SendData(CommonData.URBAN_INFO_DB_CFM, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.URBAN_PPG_CFM: {
                byte[] pData = new byte[1];
                pData[0] = 0x00;
                bSendData = CommonData.SendData(CommonData.URBAN_PPG_CFM, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.URBAN_PPG_DB_CFM: {
                byte[] pData = new byte[1];
                pData[0] = 0x00;
                bSendData = CommonData.SendData(CommonData.URBAN_PPG_DB_CFM, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.URBAN_CONDITION_CFM: {
                byte[] pData = new byte[1];
                pData[0] = 0x00;
                bSendData = CommonData.SendData(CommonData.URBAN_CONDITION_CFM, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.URBAN_GOAL_REQ: {
                byte[] pData = new byte[2];
                // Goal Step 10000
                pData[0] = 0x27;
                pData[0] = 0x10;
                bSendData = CommonData.SendData(CommonData.URBAN_GOAL_REQ, 2, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.URBAN_SLEEP_DB_CFM: {
                byte[] pData = new byte[1];
                pData[0] = 0x00;
                bSendData = CommonData.SendData(CommonData.URBAN_SLEEP_DB_CFM, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.URBAN_SLEEP_WU_CFM: {
                byte[] pData = new byte[1];
                pData[0] = 0x00;
                bSendData = CommonData.SendData(CommonData.URBAN_SLEEP_WU_CFM, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.URBAN_INFO_SYNC_START_REQ:
                bSendData = CommonData.SendData(CommonData.URBAN_INFO_SYNC_START_REQ, 0, null);
                characteristic.setValue(bSendData);
                break;

            case CommonData.URBAN_INFO_SYNC_DATA_REQ: {
                byte[] pData = new byte[1];
                pData[0] = (byte) mManboSendCnt;
                bSendData = CommonData.SendData(CommonData.URBAN_INFO_SYNC_DATA_REQ, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.URBAN_PPG_SYNC_START_REQ:
                bSendData = CommonData.SendData(CommonData.URBAN_PPG_SYNC_START_REQ, 0, null);
                characteristic.setValue(bSendData);
                break;

            case CommonData.URBAN_PPG_SYNC_DATA_REQ: {
                byte[] pData = new byte[1];
                pData[0] = (byte) mPPGSendCnt;
                bSendData = CommonData.SendData(CommonData.URBAN_PPG_SYNC_DATA_REQ, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.URBAN_SLEEP_SYNC_START_REQ:
                bSendData = CommonData.SendData(CommonData.URBAN_SLEEP_SYNC_START_REQ, 0, null);
                characteristic.setValue(bSendData);
                break;

            case CommonData.URBAN_SLEEP_SYNC_DATA_REQ: {
                byte[] pData = new byte[1];
                pData[0] = (byte) mSleepSendCnt;
                bSendData = CommonData.SendData(CommonData.URBAN_SLEEP_SYNC_DATA_REQ, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.URBAN_PPG_START_REQ:
                bSendData = CommonData.SendData(CommonData.URBAN_PPG_START_REQ, 0, null);
                characteristic.setValue(bSendData);
                break;

            case CommonData.URBAN_BARO_START_REQ:
                bSendData = CommonData.SendData(CommonData.URBAN_BARO_START_REQ, 0, null);
                characteristic.setValue(bSendData);
                break;

            // TODO: EXERCISE

            case CommonData.EXERCISE_BtoA_STOP_CFM: {
                byte[] pData = new byte[1];
                pData[0] = (byte) mSleepSendCnt;
                bSendData = CommonData.SendData(CommonData.EXERCISE_BtoA_STOP_CFM, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.EXERCISE_AtoB_START_REQ: {
                byte[] pData = new byte[1];
                pData[0] = CommonData.EXERCISE_WALKING;
                bSendData = CommonData.SendData(CommonData.EXERCISE_AtoB_START_REQ, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.EXERCISE_AtoB_STOP_REQ: {
                bSendData = CommonData.SendData(CommonData.EXERCISE_AtoB_STOP_REQ, 0, null);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.EXERCISE_INFO_CFM: {
                byte[] pData = new byte[1];
                pData[0] = (byte) mSleepSendCnt;
                bSendData = CommonData.SendData(CommonData.EXERCISE_INFO_CFM, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.EXERCISE_PPG_CFM: {
                byte[] pData = new byte[1];
                pData[0] = (byte) mSleepSendCnt;
                bSendData = CommonData.SendData(CommonData.EXERCISE_PPG_CFM, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.EXERCISE_SYNC_REQ: {
                byte[] pData = new byte[1];
                pData[0] = CommonData.EXERCISE_WALKING;
                bSendData = CommonData.SendData(CommonData.EXERCISE_SYNC_REQ, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.EXERCISE_SYNC_START_REQ: {
                byte[] pData = new byte[1];
                pData[0] = CommonData.EXERCISE_WALKING;
                bSendData = CommonData.SendData(CommonData.EXERCISE_SYNC_START_REQ, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.EXERCISE_SYNC_DATA_REQ: {
                byte[] pData = new byte[1];
                pData[0] = (byte) mExerciseHRMSendCnt;
                bSendData = CommonData.SendData(CommonData.EXERCISE_SYNC_DATA_REQ, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.EXERCISE_SYNC_DATA_INFO_REQ: {
                bSendData = CommonData.SendData(CommonData.EXERCISE_SYNC_DATA_INFO_REQ, 0, null);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.EXERCISE_SYNC_DATA_HRM_REQ: {
                byte[] pData = new byte[1];
                pData[0] = (byte) mExerciseHRMSendCnt;
                bSendData = CommonData.SendData(CommonData.EXERCISE_SYNC_DATA_HRM_REQ, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.EXERCISE_SYNC_DATA_ALTITUDE_REQ: {
                byte[] pData = new byte[1];
                pData[0] = (byte) mExerciseAltitudeSendCnt;
                bSendData = CommonData.SendData(CommonData.EXERCISE_SYNC_DATA_ALTITUDE_REQ, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            // TODO: SETTING
            case CommonData.CONNECTION_REQ: {
                byte[] pData = new byte[12];
                System.arraycopy(CommonData.DateToByteArray(), 0, pData, 0, 8);
                pData[8] = 0x2A;
                pData[9] = 0x00;
                pData[10] = (byte) 0xB4;
                pData[11] = 0x62;
                bSendData = CommonData.SendData(CommonData.CONNECTION_REQ, 12, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.RTC_REQ:
                bSendData = CommonData.SendData(CommonData.RTC_REQ, 8, CommonData.DateToByteArray());
                characteristic.setValue(bSendData);
                break;

            case CommonData.USERPROFILE_REQ: {
                byte[] pData = new byte[4];
                pData[0] = 0x2A;
                pData[1] = 0x00;
                pData[2] = (byte) 0xB4;
                pData[3] = 0x62;
                bSendData = CommonData.SendData(CommonData.USERPROFILE_REQ, 4, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.LANGUAGE_REQ: {
                byte[] pData = new byte[1];
                pData[0] = 0x00;
                bSendData = CommonData.SendData(CommonData.LANGUAGE_REQ, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.UNIT_REQ: {
                byte[] pData = new byte[3];
                pData[0] = 0x00;
                pData[1] = 0x00;
                pData[2] = 0x00;
                bSendData = CommonData.SendData(CommonData.UNIT_REQ, 3, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.VERSION_REQ:
                bSendData = CommonData.SendData(CommonData.VERSION_REQ, 0, null);
                characteristic.setValue(bSendData);
                break;

            case CommonData.USER_PPG_REQ: {
                byte[] pData = new byte[1];
                pData[0] = 0x64;
                bSendData = CommonData.SendData(CommonData.USER_PPG_REQ, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.SLEEP_TIME_REQ: {
                byte[] pData = new byte[4];
                pData[0] = 0x15;
                pData[1] = 0x1E;
                pData[2] = 0x06;
                pData[3] = 0x00;
                bSendData = CommonData.SendData(CommonData.SLEEP_TIME_REQ, 4, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.PPG_INTERVAL_REQ: {
                byte[] pData = new byte[1];
                pData[0] = 0x03;
                bSendData = CommonData.SendData(CommonData.PPG_INTERVAL_REQ, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.EXERCISE_DISPLAY_ITEM_REQ: {
                byte[] pData = new byte[6];
                pData[0] = 0x01;
                pData[1] = 0x01;
                pData[2] = 0x00;
                pData[3] = 0x00;
                pData[4] = 0x00;
                pData[5] = 0x01;
                bSendData = CommonData.SendData(CommonData.EXERCISE_DISPLAY_ITEM_REQ, 6, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.CALL_REQ:
                bSendData = CommonData.SendData(CommonData.CALL_REQ, 0, null);
                characteristic.setValue(bSendData);
                break;

            case CommonData.CALL_ACCEPT_REQ:
                bSendData = CommonData.SendData(CommonData.CALL_ACCEPT_REQ, 0, null);
                characteristic.setValue(bSendData);
                break;

            case CommonData.SMS_REQ:
                bSendData = CommonData.SendData(CommonData.SMS_REQ, 0, null);
                characteristic.setValue(bSendData);
                break;

            case CommonData.GOAL_BtoA_CFM: {
                byte[] pData = new byte[1];
                pData[0] = 0x00;
                bSendData = CommonData.SendData(CommonData.GOAL_BtoA_CFM, 1, pData);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.GOAL_AtoB_REQ:
                bSendData = CommonData.SendData(CommonData.GOAL_AtoB_REQ, 0, null);
                characteristic.setValue(bSendData);
                break;

            case CommonData.APP_NOTI_REQ:
                bSendData = CommonData.SendData(CommonData.APP_NOTI_REQ, 0, null);
                characteristic.setValue(bSendData);
                break;

            case CommonData.CUSTOM_CONTENTS_START_REQ: {
                byte[] pData = new byte[3];
                pData[0] = 0x0A;
                pData[1] = 0x01;
                pData[2] = 0x00;

                bSendData = CommonData.SendData(CommonData.CUSTOM_CONTENTS_START_REQ, 3, pData);
                characteristic.setValue(bSendData);
            }
            break;


            case CommonData.CUSTOM_CONTENTS_DATA_REQ: {
                byte[] pData = new byte[13];
                if (mCustomSendCnt == 0)
                    mCustomSendCnt = 1;
                else if (mCustomSendCnt == 1)
                    mCustomSendCnt = 2;

                if (mCustomSendCnt < 3) {
                    pData[0] = (byte) mCustomSendCnt;
                    for (int i = 1; i < 13; i++)
                        pData[i] = 0x1F;

                    bSendData = CommonData.SendData(CommonData.CUSTOM_CONTENTS_DATA_REQ, 13, pData);
                    characteristic.setValue(bSendData);
                }
            }
            break;

            case CommonData.OTA_START_REQ: {
                bSendData = CommonData.SendData(CommonData.OTA_START_REQ, 0, null);
                characteristic.setValue(bSendData);
            }
            break;

            case CommonData.OTA_DATA_REQ: {
                int index = 0;
                byte[] pData = new byte[1];
                pData[index++] = (byte) mOTASendCnt;
                pData[index++] = 0x01;
                pData[index++] = 0x02;
                pData[index++] = 0x03;
                pData[index++] = 0x04;
                pData[index++] = 0x05;
                pData[index++] = 0x06;
                pData[index++] = 0x07;
                pData[index++] = 0x08;
                pData[index++] = 0x09;
                pData[index++] = 0x0A;
                pData[index++] = 0x0B;
                pData[index++] = 0x0C;
                bSendData = CommonData.SendData(CommonData.OTA_DATA_REQ, index, pData);
                mCharacteristic = characteristic;
                characteristic.setValue(bSendData);
                startMSGTimer(CommonData.OTA_REQ_TIMER);
            }
            break;

            //TODO #.Touch Me BLE Data Packet #.Day 2020.02.06
            case CommonData.TOUCH_GTO_TEST1: {
                byte[] pData = new byte[10];
                pData[0] = 0x30;
                pData[1] = 0x31;
                pData[2] = 0x32;
                pData[3] = 0x33;
                pData[4] = 0x34;
                pData[5] = 0x35;
                pData[6] = 0x36;
                pData[7] = 0x37;
                pData[8] = 0x38;
                pData[9] = 0x39;

                bSendData = CommonData.SendData(CommonData.TOUCH_GTO_TEST1, 10, pData);
                characteristic.setValue(bSendData);
                Log.w(TAG, String.format("SEND DATA TO JDY"));
            }
            break;

            case CommonData.TOUCH_GTO_TEST2: {
                byte[] pData = new byte[6];
                pData[0] = 0x35;
                pData[1] = 0x34;
                pData[2] = 0x33;
                pData[3] = 0x32;
                pData[4] = 0x31;
                pData[5] = 0x30;

                bSendData = CommonData.touch_Data(CommonData.TOUCH_GTO_TEST2, 6, pData);
                characteristic.setValue(bSendData);
                Log.w(TAG, String.format("SEND DATA TO JDY"));
            }
            break;

            case CommonData.SETUP_MODE_SINGLE: {
                byte[] pData = new byte[6];
                pData[0] = 0x30;
                pData[1] = 0x31;
                pData[2] = 0x32;
                pData[3] = 0x33;
                pData[4] = 0x34;
                pData[5] = 0x35;

                bSendData = CommonData.touch_Data(CommonData.SETUP_MODE_SINGLE, 6, pData);
                characteristic.setValue(bSendData);
                Log.w(TAG, String.format("SINGLE MODE [DO]"));
            }
            break;

            case CommonData.SETUP_MODE_DUAL: {
                byte[] pData = new byte[6];
                pData[0] = 0x35;
                pData[1] = 0x34;
                pData[2] = 0x33;
                pData[3] = 0x32;
                pData[4] = 0x31;
                pData[5] = 0x30;

                bSendData = CommonData.touch_Data(CommonData.SETUP_MODE_DUAL, 6, pData);
                characteristic.setValue(bSendData);
                Log.w(TAG, String.format("DUAL MODE [DO]"));
            }
            break;

            case CommonData.CMD_NODE_SCAN: {
                byte[] pData = new byte[6];
                pData[0] = 0x30;
                pData[1] = 0x34;
                pData[2] = 0x33;
                pData[3] = 0x32;
                pData[4] = 0x31;
                pData[5] = 0x30;

                bSendData = CommonData.touch_Data(CommonData.CMD_NODE_SCAN, 6, pData);
                characteristic.setValue(bSendData);
                Log.w(TAG, String.format("CMD_NODE_SCAN"));
            }
            break;

            case CommonData.CMD_NODE_REGISTER: {
                byte[] pData = new byte[6];
                pData[0] = 0x31;
                pData[1] = 0x34;
                pData[2] = 0x33;
                pData[3] = 0x32;
                pData[4] = 0x31;
                pData[5] = 0x30;

                bSendData = CommonData.touch_Data(CommonData.CMD_NODE_REGISTER, 6, pData);
                characteristic.setValue(bSendData);
                Log.w(TAG, String.format("CMD_NODE_REGISTER"));
            }
            break;

            case CommonData.CMD_SCAN_FINISH: {
                byte[] pData = new byte[6];
                pData[0] = 0x35;
                pData[1] = 0x34;
                pData[2] = 0x33;
                pData[3] = 0x32;
                pData[4] = 0x31;
                pData[5] = 0x30;

                bSendData = CommonData.touch_Data(CommonData.CMD_SCAN_FINISH, 6, pData);
                characteristic.setValue(bSendData);
                Log.w(TAG, String.format("CMD_SCAN_FINISH"));
            }
            break;

            case CommonData.CMD_PLAY_NODE_DO: {
                byte[] pData = new byte[6];
                pData[0] = 0x50;    // 'P'
                pData[1] = 0x30;    // 'M'
                //pData[2] = 0x10;    // 'DO'
                pData[2] = (byte) 0x90;    // 'DO'
                //pData[2] = 0x01;    // 'LED'
                pData[3] = 0x32;
                pData[4] = 0x33;
                pData[5] = 0x34;

                //byteToBinaryString(pData[2]);


                bSendData = CommonData.header_tail_Data(CommonData.CMD_PLAY_NODE_DO, 6, pData);
                characteristic.setValue(bSendData);
                Log.w(TAG, String.format("SOUND [DO]"));
                //Log.w(TAG, String.format("pData 2" + pData[2]));
            }
            break;

            case CommonData.CMD_PLAY_NODE_RE: {
                byte[] pData = new byte[6];
                pData[0] = 0x50;    // 'P'
                pData[1] = 0x30;    // 'M'
                //pData[2] = 0x20;    // 'RE'
                pData[2] = (byte) 0xA0;    // 'RE'
                //pData[2] = 0x02;    // 'LED'
                pData[3] = 0x32;
                pData[4] = 0x31;
                pData[5] = 0x30;

                bSendData = CommonData.header_tail_Data(CommonData.CMD_PLAY_NODE_RE, 6, pData);
                characteristic.setValue(bSendData);
                Log.w(TAG, String.format("SOUND [RE]"));
            }
            break;

            case CommonData.CMD_PLAY_NODE_MI: {
                byte[] pData = new byte[6];
                pData[0] = 0x50;    // 'P'
                pData[1] = 0x30;    // 'M'
                //pData[2] = 0x30;    // 'MI'
                pData[2] = (byte) 0xB0;    // 'RE'
                //pData[2] = 0x03;    // 'LED'
                pData[3] = 0x32;
                pData[4] = 0x31;
                pData[5] = 0x30;

                bSendData = CommonData.header_tail_Data(CommonData.CMD_PLAY_NODE_MI, 6, pData);
                characteristic.setValue(bSendData);
                Log.w(TAG, String.format("SOUND [MI]"));
            }
            break;

            case CommonData.CMD_PLAY_NODE_PA: {
                byte[] pData = new byte[6];
                pData[0] = 0x50;    // 'P'
                pData[1] = 0x30;    // 'M'
                //pData[2] = 0x40;    // 'PA'
                //pData[2] = 0x10;    // 'DO'
                pData[2] = (byte) 0xC0;    // 'RE'
                //pData[2] = 0x04;    // 'LED'
                pData[3] = 0x32;
                pData[4] = 0x31;
                pData[5] = 0x30;

                bSendData = CommonData.header_tail_Data(CommonData.CMD_PLAY_NODE_PA, 6, pData);
                characteristic.setValue(bSendData);
                Log.w(TAG, String.format("SOUND [PA]"));
            }
            break;

            case CommonData.CMD_PLAY_NODE_SO: {
                byte[] pData = new byte[6];
                pData[0] = 0x50;    // 'P'
                pData[1] = 0x30;    // 'M'
                //pData[2] = 0x50;    // 'SO'
                //pData[2] = 0x20;    // 'RE'
                pData[2] = (byte) 0xD0;    // 'RE'
                //pData[2] = 0x05;    // 'LED'
                pData[3] = 0x32;
                pData[4] = 0x31;
                pData[5] = 0x30;

                bSendData = CommonData.header_tail_Data(CommonData.CMD_PLAY_NODE_SO, 6, pData);
                characteristic.setValue(bSendData);
                Log.w(TAG, String.format("SOUND [SO]"));
            }
            break;

            case CommonData.CMD_PLAY_NODE_RA: {
                byte[] pData = new byte[6];
                pData[0] = 0x50;    // 'P'
                pData[1] = 0x30;    // 'M'
                //pData[2] = 0x60;    // 'RA'
                //pData[2] = 0x30;    // 'MI'
                pData[2] = (byte) 0xE0;    // 'RE'
                //pData[2] = 0x06;    // 'LED'
                pData[3] = 0x32;
                pData[4] = 0x31;
                pData[5] = 0x30;

                bSendData = CommonData.header_tail_Data(CommonData.CMD_PLAY_NODE_RA, 6, pData);
                characteristic.setValue(bSendData);
                Log.w(TAG, String.format("SOUND [RA]"));
            }
            break;

            case CommonData.CMD_PLAY_NODE_SI: {
                byte[] pData = new byte[6];
                pData[0] = 0x50;    // 'P'
                pData[1] = 0x30;    // 'M'
                //pData[2] = 0x70;    // 'SI'
                //pData[2] = 0x40;    // 'MI'
                pData[2] = (byte) 0xF0;    // 'RE'
                //pData[2] = 0x07;    // 'LED'
                pData[3] = 0x32;
                pData[4] = 0x31;
                pData[5] = 0x30;

                bSendData = CommonData.header_tail_Data(CommonData.CMD_PLAY_NODE_SI, 6, pData);
                characteristic.setValue(bSendData);
                Log.w(TAG, String.format("SOUND [SI]"));
            }
            break;





            default:
                Log.d(TAG, "Send MSGType = " + type);
                break;

        }

        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        //sendBroadcast(sendPacketIntent);
        Log.d(TAG, "writeGattCharacteristic SUCCESS");
        return mBluetoothGatt.writeCharacteristic(characteristic);
    }

    public void startMSGTimer(int msg) {
        Log.d(TAG, "startMSGTimer [ " + msg + " ]");
        switch(msg) {
            case CommonData.OTA_REQ_TIMER:
                mRefreshHandler.sendEmptyMessageDelayed(CommonData.OTA_REQ_TIMER, CommonData.OTA_REQ_TIMER_INTERVAL);
                break;
        }
    }

    Handler mRefreshHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg){
            Log.d(TAG, "msgTimeExpired [ " + msg.what + " ]");
            switch(msg.what){

                case CommonData.OTA_REQ_TIMER:
                    mOTASendCnt++;
                    Log.i(TAG, "mOTASendCnt: " + mOTASendCnt);
                    if (mOTASendCnt < 171) {
                        byte[] bSendData;
                        int index = 0;
                        byte[] pData = new byte[1];
                        pData[index++] = (byte) mOTASendCnt++;
                        pData[index++] = 0x01;
                        pData[index++] = 0x02;
                        pData[index++] = 0x03;
                        pData[index++] = 0x04;
                        pData[index++] = 0x05;
                        pData[index++] = 0x06;
                        pData[index++] = 0x07;
                        pData[index++] = 0x08;
                        pData[index++] = 0x09;
                        pData[index++] = 0x0A;
                        pData[index++] = 0x0B;
                        pData[index++] = 0x0C;
                        bSendData = CommonData.SendData(CommonData.OTA_DATA_REQ, index, pData);
                        mCharacteristic.setValue(bSendData);
                        mRefreshHandler.sendEmptyMessageDelayed(CommonData.OTA_REQ_TIMER, CommonData.OTA_REQ_TIMER_INTERVAL);
                    } else {
                        mRefreshHandler.removeMessages(CommonData.OTA_REQ_TIMER);
                    }
                    break;


            }

            return false;
        }
    });


    public void enableTXNotification()
    {

    	/*
    	if (mBluetoothGatt == null) {
    		showMessage("mBluetoothGatt null" + mBluetoothGatt);
    		broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
    		return;
    	}
    		*/
        BluetoothGattService RxService = mBluetoothGatt.getService(RX_SERVICE_UUID);
        if (RxService == null) {
            showMessage("Rx service not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        BluetoothGattCharacteristic TxChar = RxService.getCharacteristic(TX_CHAR_UUID);
        if (TxChar == null) {
            showMessage("Tx charateristic not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        //setCharacteristicNotification(TxChar, true);
        mBluetoothGatt.setCharacteristicNotification(TxChar,true);

        BluetoothGattDescriptor descriptor = TxChar.getDescriptor(CCCD);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);


    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic,
                                    byte[] data)
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        Log.i(TAG, "characteristic " + characteristic.toString());
        if(characteristic==null)
        {
            Log.d("characteristic null","bb");
        }
        characteristic.setValue(data);
        mBluetoothGatt.writeCharacteristic(characteristic);

    }

    public void writeRXCharacteristic(byte[] value)
    {

        BluetoothGattService RxService = mBluetoothGatt.getService(RX_SERVICE_UUID);
        showMessage("mBluetoothGatt null"+ mBluetoothGatt);
        if (RxService == null) {
            showMessage("Rx service not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        BluetoothGattCharacteristic RxChar = RxService.getCharacteristic(RX_CHAR_UUID);
        if (RxChar == null) {
            showMessage("Rx charateristic not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        RxChar.setValue(value);
        boolean status = mBluetoothGatt.writeCharacteristic(RxChar);

        Log.d(TAG, "write TXchar - status=" + status);
    }

    public final static String DEVICE_DOES_NOT_SUPPORT_UART =
            "com.nordicsemi.nrfUART.DEVICE_DOES_NOT_SUPPORT_UART";

    private void showMessage(String msg) {
        Log.e(TAG, msg);
    }

    public static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"); // band
    public static final UUID RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e"); // band
    //public static final UUID RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e"); // band
    public static final UUID RX_CHAR_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"); // band
    //public static final UUID TX_CHAR_UUID = UUID.fromString("6e0400003-b5a3-f393-e0a9-e50e24dcca9e"); // band
    public static final UUID TX_CHAR_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"); //jdy bt

    public static final UUID HEALTH_DATA_UUID = UUID.fromString("0000FFC1-0000-1000-8000-00805f9b34fb"); // band
    public static final UUID StopSLEEP_DATA_UUID = UUID.fromString("0000FFC3-0000-1000-8000-00805f9b34fb"); // band

    public static final UUID TouchMe_DATA_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"); //jdy bt

}
