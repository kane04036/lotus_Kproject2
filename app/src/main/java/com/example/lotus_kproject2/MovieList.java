package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.BlockingDeque;

public class MovieList {
    private String movName, movCode, movImg, movReleaseDate, token, movGenre;
    Context context;

    public MovieList(String movCode) {
        this.movCode = movCode;
//        this.context = context;
//        this.token = token;
//        movieDetailRequest(movCode, token);
    }
    public void setData(String movName, String movImg, String movReleaseDate, String movGenre){
        this.movName = movName;
        this.movImg = movImg;
        this.movReleaseDate = movReleaseDate;
        this.movGenre = movGenre;
    }

    public String getMovName() {
        return movName;
    }

    public String getMovImg() {
        return movImg;
    }
    public String getMovGenre() {
        return movGenre;
    }

    public String getMovCode() {
        return movCode;
    }

    public String getMovReleaseDate() {
        return movReleaseDate;
    }

    private void movieDetailRequest(String movCode, String token ) {
        RequestQueue Queue = Volley.newRequestQueue(context);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", movCode);
            jsonObject.put("token", token);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = "https://flask-sample-eezgq.run.goorm.io/search_movie/detail";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "request in movieList: " + response.getString("res"));
                    JSONArray dataJsonArrayAll = response.getJSONArray("data");
                    JSONArray dataJsonArray = dataJsonArrayAll.getJSONArray(1);
//                    MovieList movie = new MovieList(dataJsonArray.getString(0),dataJsonArray.getString(1),
//                            dataJsonArray.getString(3),dataJsonArray.getString(7));
                    String runningTime = (String) dataJsonArray.get(2);
                    String country = (String) dataJsonArray.get(6);
                    String releaseData = (String) dataJsonArray.get(7);
                    String genres = dataJsonArray.getString(8);
                    genres = genres.replace(","," | ");
                    movGenre = genres;

                    movName = dataJsonArray.getString(0);
                    movImg = dataJsonArray.getString(3);
                    movReleaseDate = dataJsonArray.getString(7);
                    Log.d(TAG, "onResponse: Movie List Class: movName:"+movName);


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
