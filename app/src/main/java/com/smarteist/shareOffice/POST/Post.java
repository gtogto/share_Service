package com.smarteist.shareOffice.POST;

import com.google.gson.annotations.SerializedName;

public class Post {
    /**
     * {
     *     "userId": 1,
     *     "id": 1,
     *     "title": "sunt aut facere repellat ~~~",
     *     "body": "quia et suscipit\nsuscipit ~~~"
     * },
     */
    @SerializedName("UserID")
    private String userId;
    @SerializedName("deviceNo")
    private String deviceNo;
    @SerializedName("userName")
    private String userName;

    public Post(String userId, String deviceNo, String userName) {
        this.userId = userId;
        this.deviceNo = deviceNo;
        this.userName = userName;

    }
    /*public int getUserId() {
        return userId;
    }
    public int getId() {
        return id;
    }*/
    public String getUserId() {
        return userId;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public String getUserName() {
        return userName;
    }

}
