package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private LongReviewInDetailFragment longReviewInDetailFragment = new LongReviewInDetailFragment();
    private ShortReviewInDetailFragment shortReviewInDetailFragment = new ShortReviewInDetailFragment();

    MaterialToolbar topBarInDetail;
    TextView tvMovieName, tvGenre, tvCountry, tvRunningTime, tvReleaseDate, tvSummary, tvReadMore, tvTabBarLongReview, tvTabBarShortReview, tvDirector, tvActor, tvReadMoreActors;
    ImageView imageMovDetail, imgLike;
    String summary, summary_sub, movCode, director, actors, actors_sub;
    FrameLayout frameLayoutInDetail;
    SharedPreferences sharedPreferences;

    ArrayList<String> actorArray = new ArrayList<>();

    boolean isClose = false, summary_long = true, isLike = false, isCloseActors = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movCode = getIntent().getStringExtra("movCode");
        movieDetailRequest(movCode);

        topBarInDetail = findViewById(R.id.topBarInDetail);
        tvTabBarLongReview = findViewById(R.id.tvTabBarLongReview);
        tvTabBarShortReview = findViewById(R.id.tvTabBarShortReview);
        imageMovDetail = findViewById(R.id.imageMovieDetail);
        tvMovieName = findViewById(R.id.tvMovieName);
        tvSummary = findViewById(R.id.tvSummary);
        tvCountry = findViewById(R.id.tvCountry);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvRunningTime = findViewById(R.id.tvRunningTime);
        tvReadMore = findViewById(R.id.tvReadMore);
        frameLayoutInDetail = findViewById(R.id.frameLayoutInDetail);
        imgLike = findViewById(R.id.imgLike);
        tvDirector = findViewById(R.id.tvDirector);
        tvActor = findViewById(R.id.tvActor);
        tvReadMoreActors = findViewById(R.id.tvReadMoreActors);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayoutInDetail, longReviewInDetailFragment).commitAllowingStateLoss();

        tvReadMoreActors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCloseActors) {
                    isCloseActors = true;
                    tvActor.setText(actors);
                    tvReadMoreActors.setText("접기");
                } else {
                    isCloseActors = false;
                    tvActor.setText(actors_sub + "...");
                    tvReadMoreActors.setText("더보기");
                }
            }
        });

        tvReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isClose) {
                    isClose = true;
                    tvSummary.setText(summary);
                    tvReadMore.setText("접기");
                } else {
                    isClose = false;
                    tvSummary.setText(summary_sub + "...");
                    tvReadMore.setText("더보기");
                }
            }
        });

        tvMovieName.setText(getIntent().getStringExtra("movName"));
        Glide.with(getApplicationContext()).
                load(getIntent().getStringExtra("movImage")).error(R.drawable.gray_profile)
                .fallback(R.drawable.profile)
                .into(imageMovDetail);

        topBarInDetail.setTitle(getIntent().getStringExtra("movName"));


        topBarInDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLike) {
                    isLike = true;
                    imgLike.setImageResource(R.drawable.bookmark_filled_small);
                    preferAddRequest();
                } else {
                    isLike = false;
                    imgLike.setImageResource(R.drawable.bookmark_small);
                    preferSubRequest();
                }
            }
        });
        tvTabBarLongReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTabBarLongReview.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line_rectangle));
                tvTabBarLongReview.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                tvTabBarShortReview.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.filled_rectangle));
                tvTabBarShortReview.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkGray));

                FragmentTransaction transaction_longreview = fragmentManager.beginTransaction();
                transaction_longreview.replace(R.id.frameLayoutInDetail, longReviewInDetailFragment).commitAllowingStateLoss();
            }
        });

        tvTabBarShortReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTabBarShortReview.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line_rectangle));
                tvTabBarShortReview.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                tvTabBarLongReview.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.filled_rectangle));
                tvTabBarLongReview.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkGray));

                FragmentTransaction transaction_shortreview = fragmentManager.beginTransaction();
                transaction_shortreview.replace(R.id.frameLayoutInDetail, shortReviewInDetailFragment).commitAllowingStateLoss();
            }
        });

    }

    private void movieDetailRequest(String movCode) {
        RequestQueue Queue = Volley.newRequestQueue(MovieDetailActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", movCode);


        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.movieDetail);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: movDetail Result: " + response.getString("res"));
                    JSONArray dataJsonArray = response.getJSONArray("data");
                    director = (String) dataJsonArray.get(4);
                    String runningTime = (String) dataJsonArray.get(2);
                    String country = (String) dataJsonArray.get(6);
                    String releaseData = (String) dataJsonArray.get(7);
                    JSONArray actorJsonArray = dataJsonArray.getJSONArray(5);
                    for(int i = 0; i< actorJsonArray.length(); i++){
                        JSONObject actorObj = actorJsonArray.getJSONObject(i);
                        actorArray.add(actorObj.getString("actorNm"));
                    }
                    actors = actorArray.toString();
                    actors = actors.replaceAll("\\[","");
                    actors = actors.replaceAll("\\]","");

                    actors_sub = actors.substring(0,30);
                    tvActor.setText(actors_sub+"...");

                    summary = (String) dataJsonArray.get(9);
                    if (summary.length() > 100) {
                        summary_sub = summary.substring(0, 100);
                    } else {
                        summary_sub = summary;
                        summary_long = false;
                        tvReadMore.setText("");
                    }

                    tvSummary.setText(summary_sub + "...");
                    tvReleaseDate.setText(releaseData);
                    tvCountry.setText(country);
                    tvRunningTime.setText(runningTime + "분");
                    tvDirector.setText(director);



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

    private void preferAddRequest() {
        RequestQueue Queue = Volley.newRequestQueue(MovieDetailActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);

            jsonObject.put("contentID", movCode);
            jsonObject.put("director",director);
            jsonObject.put("actor","");
            jsonObject.put("token",sharedPreferences.getString("token",""));


        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.preferAdd);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: prefer Add Result: " + response.getString("res"));
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

    private void preferSubRequest() {
        RequestQueue Queue = Volley.newRequestQueue(MovieDetailActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", "");


        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.preferSub);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: prefer Sub Result: " + response.getString("res"));
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
