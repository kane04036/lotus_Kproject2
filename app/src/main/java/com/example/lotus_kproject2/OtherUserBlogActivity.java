package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class OtherUserBlogActivity extends AppCompatActivity {
    private LongReviewInMyBlogFragment longReviewInMyBlogFragment = new LongReviewInMyBlogFragment();
    private ShortReviewInMyBlogFragment shortReviewInMyBlogFragment = new ShortReviewInMyBlogFragment();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FrameLayout frameLayout;

    private MaterialToolbar appbar;
    private TextView tvNickname, tvMbti, tvFollowingNum, tvFollowerNum, tvTabBarLongReview, tvTabBarShortReview, btnFollow;
    private String memNum;

    Bundle result = new Bundle();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_blog);

        tvNickname = findViewById(R.id.tvNickNameInUserBlog);
        tvMbti = findViewById(R.id.tvMbtiInUserBlog);
        appbar = findViewById(R.id.topAppBarInUserBlog);
        tvTabBarLongReview = findViewById(R.id.tvTabBarLongReviewInUserBlog);
        tvTabBarShortReview = findViewById(R.id.tvTabBarShortReviewInUserBlog);
        frameLayout = findViewById(R.id.frameLayoutInUserBlog);
        btnFollow = findViewById(R.id.btnFollow);
        tvFollowerNum = findViewById(R.id.tvFollowerNumber);
        tvFollowingNum = findViewById(R.id.tvFollowingNumber);

        tvNickname.setText(getIntent().getStringExtra("nickname"));
        tvMbti.setText(getIntent().getStringExtra("mbti"));
        memNum = getIntent().getStringExtra("userId");

        requestMyPageData(memNum);

        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestFollow(memNum);
            }
        });

        result.putString("userId", memNum);
        fragmentManager.setFragmentResult("userData_long", result);
        fragmentManager.setFragmentResult("userData_short", result);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayoutInUserBlog, longReviewInMyBlogFragment).commitAllowingStateLoss();

        tvTabBarLongReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTabBarLongReview.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line_rectangle));
                tvTabBarLongReview.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                tvTabBarShortReview.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.filled_rectangle));
                tvTabBarShortReview.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayoutInUserBlog, longReviewInMyBlogFragment).commitAllowingStateLoss();
            }
        });

        tvTabBarShortReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTabBarShortReview.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line_rectangle));
                tvTabBarShortReview.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                tvTabBarLongReview.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.filled_rectangle));
                tvTabBarLongReview.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayoutInUserBlog, shortReviewInMyBlogFragment).commitAllowingStateLoss();
            }
        });
    }

    private void requestFollow(String memNum) {
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
            Log.d(TAG, "preferListRequest: token:" + sharedPreferences.getString("token", ""));
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("user_id", sharedPreferences.getString("memNum", ""));
            jsonObject.put("target_id", memNum);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.follow);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: follow request res:" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        btnFollow.setBackground(getDrawable(R.drawable.round_rectangle_gray));
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

    private void requestMyPageData(String memNum) {
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userNm", memNum);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.mypage);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: my page: res:"+response.getString("res"));
                    if(response.getString("res").equals("200")){
                        JSONArray dataArray = response.getJSONArray("data");
                        tvFollowerNum.setText(dataArray.getString(3));
                        tvFollowingNum.setText(dataArray.getString(4));
                        Log.d(TAG, "onResponse: following:"+dataArray.getString(4)+"follower:"+dataArray.getString(3));

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
