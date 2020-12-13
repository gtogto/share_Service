package com.smarteist.shareOffice.POST;
import com.google.gson.annotations.SerializedName;

public class ReservationPost {
    private String userName;
    private String deviceNo;

    public ReservationPost(String userName, String deviceNo) {

        this.userName = userName;
        this.deviceNo = deviceNo;
    }


    /*send to dApp (reserve information)*/
    public String pUserID() {
        return userName;
    }
    public String pDeviceNo() {
        return deviceNo;
    }
}
