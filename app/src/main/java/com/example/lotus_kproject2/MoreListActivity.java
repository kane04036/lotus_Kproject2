package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

import java.util.ArrayList;

public class MoreListActivity extends AppCompatActivity {
    private LongReviewBoardRecyclerViewAdapter longReviewAdapter;
    private ShortReviewBoardRecyclerViewAdapter shortReviewAdapter;
    private MovieListRecyclerViewAdapter movieAdapter;

    private MaterialToolbar appbar;
    private RecyclerView recyclerView;

    private ArrayList<ReviewDataList> shortReviewDataList = new ArrayList<>();
    private ArrayList<ReviewDataList> longReviewDataList = new ArrayList<>();
    private String keyword;
    private Integer type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_list);

        keyword = getIntent().getStringExtra("keyword");
        type = getIntent().getIntExtra("type",4);

        appbar = findViewById(R.id.topBarMoreList);
        recyclerView = findViewById(R.id.recyViewMoreList);

        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        longReviewAdapter = new LongReviewBoardRecyclerViewAdapter(getApplicationContext(), longReviewDataList);
        shortReviewAdapter = new ShortReviewBoardRecyclerViewAdapter(getApplicationContext(), shortReviewDataList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));


        switch (type){
            case 0:
                break;
            case 1:
                recyclerView.setAdapter(longReviewAdapter);
                requestLongReview(keyword);
                break;
            case 2:
                recyclerView.setAdapter(shortReviewAdapter);
                requestShortReview(keyword);
                break;
        }

    }

    private void requestShortReview(String keyword) {
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("keyword", keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.viewShortReviewKeyWord);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: short review board request:" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        JSONArray dataJsonArray = response.getJSONArray("data");
                        JSONArray likeArray = response.getJSONArray("like");
                        shortReviewDataList.clear();
                        for (int i = 0; i < dataJsonArray.length(); i++) {
                            JSONObject object = dataJsonArray.getJSONObject(i);
                            int mbtiNum = object.getInt("user_mbti");
                            Resources res = getResources();
                            String[] mbtiArray = res.getStringArray(R.array.mbti_array);

                            float star = Float.parseFloat(object.getString("star"));

                            shortReviewDataList.add(new ReviewDataList(object.getString("_id"), object.getString("movie_id"), object.getString("movie_name"),
                                    object.getString("user_id"), mbtiArray[mbtiNum],
                                    object.getString("user_nickname"), object.getString("writing"), star,likeArray.getString(i)));
                        }
                        shortReviewAdapter.notifyDataSetChanged();
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

    private void requestLongReview(String keyword) {
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("keyword",keyword );
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.viewLongReviewKeyWord);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: request long review:"+response.getString("res"));

                    if(response.getString("res").equals("200")){
                        JSONArray dataJsonArray = response.getJSONArray("data");
                        JSONArray likeArray = response.getJSONArray("like");
                        longReviewDataList.clear();

                        for(int i = 0; i<dataJsonArray.length(); i++){
                            JSONObject object = dataJsonArray.getJSONObject(i);
                            int mbtiNum = object.getInt("user_mbti");
                            Resources res = getResources();
                            String[] mbtiArray = res.getStringArray(R.array.mbti_array);

                            longReviewDataList.add(new ReviewDataList(object.getString("_id"), object.getString("movie_id"), object.getString("movie_name"),
                                    object.getString("title"), object.getString("user_id"), mbtiArray[mbtiNum],
                                    object.getString("user_nickname"), object.getString("writing"), likeArray.getString(i)));
                        }
                        longReviewAdapter.notifyDataSetChanged();
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
