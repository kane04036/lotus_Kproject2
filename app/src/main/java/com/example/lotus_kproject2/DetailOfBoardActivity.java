package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
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
    private String writingId, movCode, movImgUrl, movName, movDate;
    private TextView tvTitle, tvMbti, tvNickname, tvWriting, tvMovName, tvMovDate, tvThumbUpNum;
    private ImageView imgMov, imgThumbUP;
    private MaterialToolbar appbar;
    private Boolean isMine = false, isLike = false;
    private ReviewDataList reviewData;
    private ProgressBar progressBar;

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_review_detail_of_board);
        overridePendingTransition(0, 0);

        tvTitle = findViewById(R.id.tvLongReviewTitleInDetailOfBoard);
        tvMbti = findViewById(R.id.tvMbtiInLongReviewDetailOfBoard);
        tvNickname = findViewById(R.id.tvNickNameShortReviewInDetailOfBoard);
        tvWriting = findViewById(R.id.tvWritingInDetailOfBoard);
        appbar = findViewById(R.id.topAppBarInDetailOfBoard);
        tvMovName = findViewById(R.id.tvMovNameInMovInfo);
        tvMovDate = findViewById(R.id.tvMovDateInMovInfo);
        imgMov = findViewById(R.id.imgMovInMovInfo);
        tvThumbUpNum = findViewById(R.id.tvThumbUpNumInDetailOfBoard);
        progressBar = findViewById(R.id.progressInDetailOfBoard);
        imgThumbUP = findViewById(R.id.imgThumbUpInDetailOfBoard);


        writingId = getIntent().getStringExtra("writingId");
        movCode = getIntent().getStringExtra("movCode");
        requestMovInfo(movCode);
        requestReview(writingId);
        Log.d(TAG, "writingID: "+writingId);

        imgMov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                intent.putExtra("movCode",movCode);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        tvMovName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                intent.putExtra("movCode",movCode);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });



        imgThumbUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLike)
                    requestLikeCancel(writingId);
                else
                    requestLikeAdd(writingId);
            }
        });

        tvNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OtherUserBlogActivity.class);
                intent.putExtra("nickname", reviewData.getNickname());
                intent.putExtra("mbti", reviewData.getMbti());
                intent.putExtra("userId", reviewData.getUserId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(0, 0);
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
                switch (item.getItemId()) {
                    case R.id.menu_more:
                        if (isMine) {
                            showPopupMenuMe();
                        } else {
                            showPopupMenuOtherUser();
                        }
                        break;
                }
                return false;
            }
        });


    }

    private void reportReview(String writingId) {
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("boardID", writingId);
            jsonObject.put("tp", "1");

            Log.d(TAG, "reportReview: token, id, tp" + sharedPreferences.getString("token", "") + "    " + writingId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.report);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: report: res" + response.getString("res"));

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

    private void requestMovInfo(String movCode) {
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
            jsonObject.put("seq", movCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.thumbnail_seq);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: thumbnail seq : res" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        JSONArray dataArray = response.getJSONArray("data");
                        movName = dataArray.getString(1);
                        movImgUrl = dataArray.getString(3);
                        movDate = dataArray.getString(4);
                        Log.d(TAG, "onResponse: thumbnail seq img url:" + movImgUrl);
                        tvMovName.setText(movName);
                        Glide.with(getApplicationContext()).load(movImgUrl).error(R.drawable.gray_profile).into(imgMov);
                        if (!movDate.isEmpty()) {
                            tvMovDate.setText(movDate.substring(0, 4));
                        } else {
                            tvMovDate.setText("개봉일자불명");
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

    private void showPopupMenuOtherUser() {
        final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), appbar, Gravity.END);
        getMenuInflater().inflate(R.menu.popup_menu_in_writing, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_report:
                        reportReview(writingId);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();

    }

    private void showPopupMenuMe() {
        final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), appbar, Gravity.END);
        getMenuInflater().inflate(R.menu.popup_menu_short_review_in_myblog, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_modify_in_myBlog:
                        Intent intent = new Intent(getApplicationContext(), ModifyLongReviewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("title", reviewData.getTitle());
                        bundle.putString("writing", reviewData.getWriting());
                        bundle.putString("movName", reviewData.getMovName());
                        bundle.putString("writingId", reviewData.getWritingId());
                        bundle.putString("movCode",reviewData.getMovId());
                        intent.putExtra("reviewData", bundle);
                        startActivity(intent);
                        break;
                    case R.id.menu_delete_in_myBlog:
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailOfBoardActivity.this);
                        builder.setMessage("감상평을 삭제하시겠습니?");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteReview(writingId);
                            }
                        });
                        builder.setNegativeButton("취소", null);
                        builder.show();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();

    }

    private void deleteReview(String boardId) {
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("boardID", boardId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.deleteLongReview);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: delete: res" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        onBackPressed();
                        finish();
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

    private void requestReview(String writingId) {
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.loginData),Context.MODE_PRIVATE);
            jsonObject.put("board_id", writingId);
            jsonObject.put("token",sharedPreferences.getString("token",""));
            Log.d(TAG, "requestReview: board id:"+writingId+"  token:"+sharedPreferences.getString("token",""));

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.viewLongReviewWritingId);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: request long review with writing id: res" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        progressBar.setVisibility(View.INVISIBLE);
                        JSONObject reviewObject = response.getJSONObject("data");

                        Resources res = getResources();
                        String[] mbtiArray = res.getStringArray(R.array.mbti_array);
                        String userMbti = mbtiArray[reviewObject.getInt("user_mbti")];

                        reviewData = new ReviewDataList(reviewObject.getString("_id"), reviewObject.getString("movie_id"),
                                reviewObject.getString("movie_name"), reviewObject.getString("title"),
                                reviewObject.getString("user_id"), userMbti,
                                reviewObject.getString("user_nickname"), reviewObject.getString("writing"),
                                response.getInt("like"), response.getString("isLike"));

                        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
                        if (reviewData.getUserId().equals(sharedPreferences.getString("memNum", ""))) {
                            isMine = true;
                        } else {
                            isMine = false;
                        }

                        tvTitle.setText(reviewData.getTitle());
                        tvWriting.setText(reviewData.getWriting());
                        tvNickname.setText(reviewData.getNickname());
                        tvMbti.setText(reviewData.getMbti());
                        tvThumbUpNum.setText(String.valueOf(reviewData.getLikeNum()));
                        if(reviewData.getIsLike().equals("1")){
                            imgThumbUP.setImageResource(R.drawable.thumb_up_filled_medium);
                            isLike = true;
                        }else{
                            imgThumbUP.setImageResource(R.drawable.thumb_up_medium);
                            isLike = false;
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
    private void requestLikeAdd(String boardId){
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("board_id", boardId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.longLikeAdd);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: board like: res" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        imgThumbUP.setImageResource(R.drawable.thumb_up_filled_medium);
                        Integer likeNum = reviewData.getLikeNum() +1;
                        tvThumbUpNum.setText(String.valueOf(likeNum));
                        reviewData.setLikeNum(likeNum);
                        reviewData.setIsLike("1");
                        isLike = true;
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
    private void requestLikeCancel(String boardId){
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("board_id", boardId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.longLikeCancel);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: board like cancel: res" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        imgThumbUP.setImageResource(R.drawable.thumb_up_medium);
                        Integer likeNum = reviewData.getLikeNum() -1;
                        tvThumbUpNum.setText(String.valueOf(likeNum));
                        reviewData.setLikeNum(likeNum);
                        reviewData.setIsLike("0");
                        isLike = false;
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

    @Override
    protected void onRestart() {
        super.onRestart();
        requestMovInfo(movCode);
        requestReview(writingId);
    }
}
