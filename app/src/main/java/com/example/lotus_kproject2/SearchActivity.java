package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity implements View.OnKeyListener {
    EditText edtSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.horizontalexit, R.anim.none);
        setContentView(R.layout.activity_search);

        edtSearch = findViewById(R.id.edtSearch);

        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == keyEvent.ACTION_DOWN) && (keyCode == keyEvent.KEYCODE_ENTER)) {
//                    searchRequest(edtSearch.getText().toString());
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity_main.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });


    }

    public void searchRequest(String textSearch) {
        RequestQueue Queue = Volley.newRequestQueue(SearchActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", textSearch);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.url) + "/test/search/naver_movie";
        Log.d(TAG, "loginRequest: url: " + URL);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String name = response.getString("name");
                    String director = response.getString("director");
                    String actor = response.getString("actor");
                    String rating = response.getString("rating");
                    Log.d(TAG, "onResponse: name: " +name);
                    Log.d(TAG, "onResponse: director:"+director);
                    Log.d(TAG, "onResponse: actor: "+actor);
                    Log.d(TAG, "onResponse: rating: "+rating);


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
    public void finish() {
        super.finish();
        if (isFinishing())
            overridePendingTransition(R.anim.none, R.anim.horizontalenter);

    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyCode == keyEvent.KEYCODE_ENTER) {
            Log.d(TAG, "onKey: enterKey pressed");
            searchRequest(edtSearch.getText().toString());
        }

        return false;
    }
}
