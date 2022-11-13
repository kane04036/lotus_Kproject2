package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class SearchMovForChooseActivity extends AppCompatActivity {
    private RecyclerView recyclerViewInMovSearch;
    private RelativeLayout layoutSearchMov;
    private EditText edtSearchMov;
    private MovieListRecyclerViewAdapter recyclerViewAdapter;
    private MaterialToolbar reviewAppBar;
    private TextView tvCancel;

    private ArrayList<String> imgArray = new ArrayList<>();
    private ArrayList<String> nameArray = new ArrayList<>();
    private ArrayList<String> codeArray = new ArrayList<>();
    private ArrayList<String> yearArrray = new ArrayList<>();

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mov);

        recyclerViewInMovSearch = findViewById(R.id.recyclerViewInMovSearch);
        layoutSearchMov = findViewById(R.id.layoutSearchMov);
        edtSearchMov = findViewById(R.id.edtSearchMov);
        reviewAppBar = findViewById(R.id.topAppBarInEditor);
        tvCancel = findViewById(R.id.tvCancelInMovSelect);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        recyclerViewAdapter = new MovieListRecyclerViewAdapter(getApplicationContext(), nameArray, imgArray, codeArray, yearArrray);
        recyclerViewInMovSearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerViewInMovSearch.setAdapter(recyclerViewAdapter);

        edtSearchMov.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == keyEvent.ACTION_DOWN) && (keyCode == keyEvent.KEYCODE_ENTER)) {
                    String searchWord = edtSearchMov.getText().toString();
                    movieSearch(searchWord);
                    return true;
                }
                return false;
            }
        });


    }


    public void movieSearch(String searchWord){
        RequestQueue Queue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", searchWord);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + "/search_movie/thumbnail";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: res:"+response.getString("res"));
                    JSONArray dataJsonArray = response.getJSONArray("data");
                    JSONArray nameJsonArray = dataJsonArray.getJSONArray(1);
                    JSONArray codeJsonArray = dataJsonArray.getJSONArray(2);
                    JSONArray imageJsonArray = dataJsonArray.getJSONArray(3);
                    JSONArray yearJsonArray = dataJsonArray.getJSONArray(4);

                    nameArray.clear(); imgArray.clear(); codeArray.clear(); yearArrray.clear();
                    for(int i = 0; i< nameJsonArray.length(); i++){
                        nameArray.add(String.valueOf(nameJsonArray.get(i)));
                        imgArray.add(String.valueOf(imageJsonArray.get(i)));
                        codeArray.add(String.valueOf(codeJsonArray.get(i)));
                        yearArrray.add(String.valueOf(yearJsonArray.get(i)));
                    }
                    recyclerViewAdapter.notifyDataSetChanged();
                    hideKeyboard();

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
    private void hideKeyboard()
    {
        if (getApplicationContext() != null && getCurrentFocus() != null)
        {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
