package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageButton btnKakaoLogin;
    TextView tvLook;
    String token, memNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLook = findViewById(R.id.tvLook);
        btnKakaoLogin = findViewById(R.id.btnKakaoLogin);

        tvLook.setPaintFlags(tvLook.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
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
                }
                else {
                    accountLogin();
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
                token = oAuthToken.getAccessToken();
                getUserInfo();
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
                token = oAuthToken.getAccessToken();
                getUserInfo();
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
                memNum = user.getId().toString();
                loginRequest();
            }
            return null;
        });
    }
    public void loginRequest(){
        RequestQueue Queue = Volley.newRequestQueue(MainActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("num", memNum);
            jsonObject.put("token", token);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.url) + getString(R.string.kakaoLogin);
        Log.d(TAG, "loginRequest: url: "+ URL);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String res = response.getString("res");
                    int isNew = response.getInt("isNew");
                    Log.d(TAG, "onResponse: res, isnew: " + res + "and"+  String.valueOf(isNew));
                    if(res.equals("200")){
                        if(isNew == 1) {
                            Intent intent = new Intent(MainActivity.this, UserInfoActivity1.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Queue.add(jsonObjectRequest);
    }


}
