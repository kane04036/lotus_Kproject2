package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;
import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

public class ReviewEditorActivity extends AppCompatActivity {
    private EditText edtTitle, edtReview;
    private TextView tvChooseMov;
    private String movName, movCode;
    private MaterialToolbar topbar;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra("movName")) {
            movName = intent.getStringExtra("movName");
            movCode = intent.getStringExtra("movCode");
            tvChooseMov.setText(movName);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_editor);


        edtTitle = findViewById(R.id.edtTitle);
        edtReview = findViewById(R.id.edtReview);
        tvChooseMov = findViewById(R.id.tvChooseMov);
        topbar = findViewById(R.id.topAppBarInEditor);
        progressBar = findViewById(R.id.progressInEditor);

        topbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_upload:
                        progressBar.setVisibility(View.VISIBLE);
                        longReviewWrite();
                }
                return false;
            }
        });
        topbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvChooseMov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewEditorActivity.this, SearchMovForChooseActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });


    }

    void longReviewWrite() {
        RequestQueue Queue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);

            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("writing", edtReview.getText().toString());
            jsonObject.put("movie_id", movCode);
            jsonObject.put("title", edtTitle.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.writeLongReview);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    SharedPreferences sharedPreferences_user = getSharedPreferences(getString(R.string.userData), Context.MODE_PRIVATE);
                    SharedPreferences sharedPreferences_login = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);

                    Log.d(TAG, "onResponse: res:" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        progressBar.setVisibility(View.INVISIBLE);
                        topbar.setEnabled(false);
                        Intent intent = new Intent(getApplicationContext(), DetailOfBoardActivity.class);
                        intent.putExtra("writingId", response.getString("data"));
                        intent.putExtra("movCode", movCode);
                        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
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

}
