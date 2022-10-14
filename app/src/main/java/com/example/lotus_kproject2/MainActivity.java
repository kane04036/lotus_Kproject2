package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.engine.Resource;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.AccessTokenInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {
    ImageButton btnKakaoLogin;
    TextView tvLook;
    private String token, memNum, isNew;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        KakaoSdk.init(this, getString(R.string.nativeAppKey));
//        kakaoDelete();
        tokenCheckFirst();
//        accessTokenInfo();
        setContentView(R.layout.activity_main);


        tvLook = findViewById(R.id.tvLook);
        btnKakaoLogin = findViewById(R.id.btnKakaoLogin);

        tvLook.setPaintFlags(tvLook.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", "775PQ319abhja9");
                editor.putString("memNum", "1111111111");
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("isNew", "0");
                startActivity(intent);
            }
        });

        btnKakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)) {
                    login();
                } else {
                    accountLogin();
                }
            }
        });
    }

    public void login() {
        String TAG = "login()";
        UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, (oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "login: 로그인 성공(토큰): " + oAuthToken.getAccessToken());
                token = oAuthToken.getAccessToken();
                UserApiClient.getInstance().me((user, throwable) -> {
                    if(error != null){
                        Log.d(TAG, "login: error");
                    }else if(user != null){
                        memNum = user.getId().toString();
                        kakaoLoginRequest();
                    }
                    return null;
                });
            }
            return null;
        });
    }

    public void accountLogin() {
        String TAG = "accountLogin()";
        UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, (oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "account : 로그인 성공(토큰): " + oAuthToken.getAccessToken());
                token = oAuthToken.getAccessToken();
                UserApiClient.getInstance().me((user, throwable) -> {
                    if(error != null){
                        Log.d(TAG, "login: error");
                    }else if(user != null){
                        memNum = user.getId().toString();
                        kakaoLoginRequest();
                    }
                    return null;
                });
            }
            return null;
        });
    }

    void tokenCheckFirst() {
        UserApiClient.getInstance().accessTokenInfo((tokenInfo, tokenError)->{
            if(tokenError != null){
                Log.d(TAG, "tokenCheckFirst: 토큰 정보 보기 실패 "+tokenError);
            }
            else if(tokenInfo != null){
                Log.d(TAG, "tokenCheckFirst: 토큰 정보보기 성공");

//                sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("token",token );
//                editor.putString("memNum", tokenInfo.getId().toString());
//                editor.commit();
                Log.d(TAG, "tokenCheckFirst: memNum"+tokenInfo.getId().toString());
                Log.d(TAG, "tokenCheckFirst: token:"+tokenInfo.toString());
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("isNew", "0");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
            return null;
        });
    }

    void tokenCheckSecond(){
        UserApiClient.getInstance().accessTokenInfo((tokenInfo, tokenError)->{
            if(tokenError != null){
                Log.d(TAG, "tokenCheckSecond: 토큰 정보 보기 실패 "+tokenError);
            }
            else if(tokenInfo != null){
                Log.d(TAG, "tokenCheckSecond: 토큰 정보보기 성공");
                memNum = tokenInfo.getId().toString();
                kakaoLoginRequest();
            }
            return null;
        });
    }

    public void kakaoDelete() {
        RequestQueue Queue = Volley.newRequestQueue(MainActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);


        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + "/umanager/delete/kakao";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: kakaoDelete: " + response.getString("res"));

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

    void kakaoLoginRequest(){
        RequestQueue Queue = Volley.newRequestQueue(MainActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("num", memNum);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.kakaoLogin);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    isNew = response.getString("isNew");
                    if(response.getString("res").equals("200")){
                        if(isNew.equals("0")){
                            int mbtiNum = response.getInt("mbti");
                            Resources res = getResources();
                            String[] mbtiArray = res.getStringArray(R.array.mbti_array);
                            String userMbti = mbtiArray[mbtiNum];

                            sharedPreferences = getSharedPreferences(getString(R.string.userData),Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
                            editor1.putString("nickname",response.getString("nickname"));
                            editor1.putString("mbti", userMbti);
                            editor1.commit();
                        }
                        sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", token);
                        editor.putString("memNum", memNum);
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("isNew", isNew);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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

    private void accessTokenInfo(){
        UserApiClient.getInstance().accessTokenInfo(new Function2<AccessTokenInfo, Throwable, Unit>() {
            @Override
            public Unit invoke(AccessTokenInfo accessTokenInfo, Throwable throwable) {
                Log.d(TAG, "accessTokenInfo: "+accessTokenInfo.toString());
                return null;
            }
        });
    }

}
