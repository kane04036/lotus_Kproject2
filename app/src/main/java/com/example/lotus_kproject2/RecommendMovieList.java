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

public class RecommendMovieList {
    private String movName, movCode, movImg, movReleaseDate, movGenre;
    Context context;

    public RecommendMovieList(String movCode, String movName, String movImg, String movGenre) {
        this.movCode = movCode;
        this.movName = movName;
        this.movImg = movImg;
        this.movGenre = movGenre;
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
        movGenre = movGenre.replaceAll(","," | ");
        movGenre = movGenre.replaceAll("/"," | ");

        return movGenre;
    }

    public String getMovCode() {
        return movCode;
    }

    public String getMovReleaseDate() {
        return movReleaseDate;
    }

}
