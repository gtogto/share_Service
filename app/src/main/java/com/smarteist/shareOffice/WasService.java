package com.smarteist.shareOffice;

import com.google.gson.JsonObject;
import com.smarteist.shareOffice.POST.Post;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WasService {
//    @GET("/api/mobileTest?deviceNo={deviceNo}")
//    Call<JsonObject> getWasData(@Query("deviceNo") String deviceNo);
    @GET("/api/mobileTest")
    Call<JsonObject> getWasData(@Query("deviceNo") int deviceNo, @Query("st") int st);

    @POST("/api/setStatus")
    Call<Post>createPost(@Body Post post);
}