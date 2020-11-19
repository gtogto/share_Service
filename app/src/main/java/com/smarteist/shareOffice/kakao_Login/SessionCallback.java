package com.smarteist.shareOffice.kakao_Login;

import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.smarteist.autoimageslider.SliderView.TAG;

public class SessionCallback implements ISessionCallback {
    // 로그인에 성공한 상태
    @Override
    public void onSessionOpened() {
        Log.i("KAKAO_SESSION", "로그인 성공!");

    }

    // 로그인에 실패한 상태
    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        Log.e("KAKAO_SESSION", "로그인 실패", exception);
    }

    // 사용자 정보 요청
    private void requestMe() {
        System.out.println("requestMe function");

        List<String> keys = new ArrayList<>();
        keys.add("properties.nickname");
        keys.add("properties.profile_image");
        keys.add("kakao_account.email");

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d(TAG,"카카오 세션 Close!");
            }

            @Override
            public void onSuccess(MeV2Response response) {
                System.out.println("onSuccess in requestMe");
                Logger.d("user id : " + response.getId());
                Logger.d("email: " + response.getKakaoAccount().getEmail());

            }

        });
    }
    /*
    public void requestMe() {
        System.out.println("requestMe function");
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d(TAG,"카카오 세션 Close!");
            }

            @Override
            public void onSuccess(MeV2Response result) {
                System.out.println("onSuccess in requestMe");
                Log.e("SessionCallback :: ", "onSuccess");

                String nickname = result.getNickname();                 // user name
                String profileImagePath = result.getProfileImagePath(); // profile picture
                String thumnailPath = result.getThumbnailImagePath();

                long id = result.getId();

                Log.e("Profile : ", nickname + "");
                Log.e("Profile : ", profileImagePath  + "");
                Log.e("Profile : ", thumnailPath + "");
                Log.e("Profile : ", id + "");
            }
        });
    }*/
}


