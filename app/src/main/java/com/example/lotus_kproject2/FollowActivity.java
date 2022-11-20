package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;
import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class FollowActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Integer type;
    private String userId;
    private ArrayList<UserDataList> dataLists = new ArrayList<>();
    private FollowRecyclerViewAdapter adapter;
    private MaterialToolbar appbar;
    private ProgressBar progressBar;
    private TextView tvErrorMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_list);
        recyclerView = findViewById(R.id.recyViewMoreList);
        appbar = findViewById(R.id.topBarMoreList);
        progressBar = findViewById(R.id.progressInMoreList);
        tvErrorMessage = findViewById(R.id.tvFollowErrorMessage);

        adapter = new FollowRecyclerViewAdapter(FollowActivity.this ,dataLists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        type = getIntent().getIntExtra("type", 5);
        userId = getIntent().getStringExtra("userId");
        Log.d(TAG, "onCreate: userId" + userId);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
        Log.d(TAG, "onCreate: token" + sharedPreferences.getString("token", ""));

        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        switch (type) {
            case 0:
                requestFollowingList(userId);
                break;
            case 1:
                requestFollowerList(userId);
                break;
        }
    }

    private void requestFollowerList(String userId) {
        RequestQueue Queue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
            jsonObject.put("user_id", userId);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            Log.d(TAG, "requestFollowerList: user id, token:"+userId+" "+sharedPreferences.getString("token",""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.viewFollower);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: follower res:" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        JSONArray userIdArray = response.getJSONArray("data");
                        JSONArray nicknameMbtiArray = response.getJSONArray("data2");
                        JSONArray isFollowArray = response.getJSONArray("isfollow");
                        if (getApplicationContext() == null) {
                            return;
                        }
                        Resources res = getResources();
                        String[] mbtiArray = res.getStringArray(R.array.mbti_array);

                        dataLists.clear();
                        for (int i = 0; i < userIdArray.length(); i++) {
                            JSONArray userIdObject = userIdArray.getJSONArray(i);
                            JSONArray nicknameMbtiObject = nicknameMbtiArray.getJSONArray(i);
                            Log.d(TAG, "onResponse: nickname, mbti, userid:" + nicknameMbtiObject.getString(0) + " " + mbtiArray[nicknameMbtiObject.getInt(1)] + " " + userIdObject.getString(0));
                            dataLists.add(new UserDataList(nicknameMbtiObject.getString(0), mbtiArray[nicknameMbtiObject.getInt(1)], userIdObject.getString(0), isFollowArray.getString(i)));
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        adapter.notifyDataSetChanged();
                    }else if(response.getString("res").equals("201")){
                        tvErrorMessage.setText("팔로워가 없습니다.");
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

    private void requestFollowingList(String userId) {
        RequestQueue Queue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
            jsonObject.put("user_id", userId);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            Log.d(TAG, "requestFollowerList: user id, token:"+userId+" "+sharedPreferences.getString("token",""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.viewFollowing);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: following res:" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        JSONArray userIdArray = response.getJSONArray("data");
                        JSONArray nicknameMbtiArray = response.getJSONArray("data2");
                        JSONArray isFollowArray = response.getJSONArray("isfollow");
                        Log.d(TAG, "onResponse: useridarray:" + userIdArray);
                        Log.d(TAG, "onResponse: useridArray size: " + userIdArray.length());
                        if (getApplicationContext() == null) {
                            return;
                        }
                        Resources res = getResources();
                        String[] mbtiArray = res.getStringArray(R.array.mbti_array);

                        dataLists.clear();
                        for (int i = 0; i < userIdArray.length(); i++) {
                            JSONArray userIdObject = userIdArray.getJSONArray(i);
                            JSONArray nicknameMbtiObject = nicknameMbtiArray.getJSONArray(i);
                            Log.d(TAG, "onResponse: nickname, mbti, userid:" + nicknameMbtiObject.getString(0) + " " + mbtiArray[nicknameMbtiObject.getInt(1)] + " " + userIdObject.getString(0));
                            dataLists.add(new UserDataList(nicknameMbtiObject.getString(0), mbtiArray[nicknameMbtiObject.getInt(1)], userIdObject.getString(0),isFollowArray.getString(i)));
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        adapter.notifyDataSetChanged();

                    }else if(response.getString("res").equals("201")){
                        tvErrorMessage.setText("팔로우하고 있는 사용자가 없습니다.");
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
