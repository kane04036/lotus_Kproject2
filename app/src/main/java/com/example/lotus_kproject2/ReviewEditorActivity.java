package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

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
    EditText edtTitle, edtReview;
    TextView tvChooseMov;
    String movName, movCode;
    private MaterialToolbar topbar;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_editor);


        edtTitle = findViewById(R.id.edtTitle);
        edtReview = findViewById(R.id.edtReview);
        tvChooseMov = findViewById(R.id.tvChooseMov);
        topbar = findViewById(R.id.topAppBarInEditor);



        if (getIntent().getIntExtra("existMovie", 0) == 1) {
            movName = getIntent().getStringExtra("movName");
            movCode = getIntent().getStringExtra("movCode");
            if (getIntent().getStringExtra("title") != null)
                edtTitle.setText(String.valueOf(getIntent().getStringExtra("title")));
            if (getIntent().getStringExtra("contents") != null)
                edtReview.setText(String.valueOf(getIntent().getStringExtra("contents")));
            tvChooseMov.setText(movName);

        }

        topbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_upload:
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
                intent.putExtra("title", edtTitle.getText().toString());
                intent.putExtra("contents", edtReview.getText().toString());
                startActivity(intent);
                finish();
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
                    Log.d(TAG, "onResponse: res:" + response.getString("res"));
                    if(response.getString("res").equals("200")){
                        Toast.makeText(getApplicationContext(),"리뷰가 성공적으로 등록됐습니다.",Toast.LENGTH_SHORT).show();
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
