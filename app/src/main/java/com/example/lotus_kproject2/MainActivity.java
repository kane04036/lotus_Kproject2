package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.UserApiClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

public class MainActivity extends AppCompatActivity {
//    Button btnLogin;
//    EditText edtLoginID, edtLoginPW;
    ImageButton btnKakaoLogin;
    TextView tvLook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // btnLogin = findViewById(R.id.btnLogin);
       // edtLoginID = findViewById(R.id.edtLoginId);
        //edtLoginPW = findViewById(R.id.edtLoginPw);
        tvLook = findViewById(R.id.tvLook);
        //tvRegister = findViewById(R.id.tvRegister);
        btnKakaoLogin = findViewById(R.id.btnKakaoLogin);

        tvLook.setPaintFlags(tvLook.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
//        tvRegister.setPaintFlags(tvRegister.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        tvLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });




        KakaoSdk.init(this, "8f9dcb3da65da193cccf8411c39b754c");



        btnKakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)){
                    login();
                    getUserInfo();
                }
                else {
                    accountLogin();
                    getUserInfo();
                }
            }
        });
    }
    public void login(){
        String TAG = "login()";
        UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this,(oAuthToken, error) -> {
            if(error != null){
                Log.e(TAG, "로그인 실패", error);
            }
            else if(oAuthToken != null){
                Log.i(TAG, "login: 로그인 성공(토큰): " + oAuthToken.getAccessToken());
            }
            return null;
        });
    }
    public void accountLogin(){
        String TAG = "accountLogin()";
        UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this,(oAuthToken, error) -> {
            if(error != null){
                Log.e(TAG, "로그인 실패", error);
            }
            else if(oAuthToken != null){
                Log.i(TAG, "account : 로그인 성공(토큰): " + oAuthToken.getAccessToken());
            }
            return null;
        });
    }
    public void getUserInfo(){
        String TAG = "getUserInfo";
        UserApiClient.getInstance().me((user, meError)->{
            if(meError != null){
                Log.e(TAG, "getUserInfo: 사용자 정보 요청 실패", meError);
            }
            else{
                Log.d(TAG, "getUserInfo: 로그인 완료");
                Log.d(TAG, "getUserInfo: user: "+user.getId().toString());

            }
            return null;
        });
    }

}
