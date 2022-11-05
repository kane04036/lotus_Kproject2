package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailOfBoardActivity extends AppCompatActivity {
    private String writingId, userId;
    private TextView tvTitle, tvMbti, tvNickname, tvWriting, tvMovName, tvMovDate, tvThumbUpNum;
    private ImageView imgMov;
    private MaterialToolbar appbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_review_detail_of_board);

        tvTitle = findViewById(R.id.tvLongReviewTitleInDetailOfBoard);
        tvMbti = findViewById(R.id.tvMbtiInLongReviewDetailOfBoard);
        tvNickname = findViewById(R.id.tvNickNameShortReviewInDetailOfBoard);
        tvWriting = findViewById(R.id.tvWritingInDetailOfBoard);
        appbar = findViewById(R.id.topAppBarInDetailOfBoard);
        tvMovName = findViewById(R.id.tvMovNameInMovInfo);
        tvMovDate = findViewById(R.id.tvMovDateInMovInfo);
        imgMov = findViewById(R.id.imgMovInMovInfo);
        tvThumbUpNum = findViewById(R.id.tvThumbUpNumInDetailOfBoard);

        String movName = getIntent().getStringExtra("movName");

        writingId = getIntent().getStringExtra("boardId");
        tvTitle.setText(getIntent().getStringExtra("title"));
        tvWriting.setText(getIntent().getStringExtra("writing"));
        tvNickname.setText(getIntent().getStringExtra("nickname"));
        tvMbti.setText(getIntent().getStringExtra("mbti"));
        userId = getIntent().getStringExtra("userId");
        tvMovName.setText(movName);
        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("movImg")).error(R.drawable.gray_profile).into(imgMov);
        tvThumbUpNum.setText(getIntent().getStringExtra("likeNum"));

        if(!getIntent().getStringExtra("movDate").isEmpty()){
            tvMovDate.setText(getIntent().getStringExtra("movDate").substring(0,4));

        }

        tvNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OtherUserBlogActivity.class);
                intent.putExtra("nickname", getIntent().getStringExtra("nickname"));
                intent.putExtra("mbti", getIntent().getStringExtra("mbti"));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        appbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_more:
                        showPopUpMenu();
                        break;
                }
                return false;
            }
        });




    }
    private void showPopUpMenu(){
        final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),appbar ,Gravity.END);
        getMenuInflater().inflate(R.menu.popup_menu_in_writing, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_report:
                        reportReview();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    private void reportReview(){
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences  sharedPreferences = getSharedPreferences(getString(R.string.loginData),Context.MODE_PRIVATE);
            jsonObject.put("token",sharedPreferences.getString("token","") );
            jsonObject.put("boardID", writingId);
            jsonObject.put("tp","1");

            Log.d(TAG, "reportReview: token, id, tp" + sharedPreferences.getString("token","")+"    "+writingId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.report);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: report: res"+response.getString("res"));

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
