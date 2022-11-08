package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifyLongReviewActivity extends AppCompatActivity {
    private EditText edtTitle, edtWriting;
    private MaterialToolbar appbar;
    private String boardId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_long_review);

        edtTitle = findViewById(R.id.edtTitleInModifyLongReview);
        edtWriting = findViewById(R.id.edtReviewModifyLongReview);
        appbar = findViewById(R.id.topAppBarInModifyLongReview);

        Bundle bundle = new Bundle();
        bundle = getIntent().getBundleExtra("reviewData");

        appbar.setTitle(bundle.getString("movName"));
        edtTitle.setText(bundle.getString("title"));
        edtWriting.setText(bundle.getString("writing"));

        boardId = bundle.getString("writingId");

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
                    case R.id.menu_upload:
                        requestModifyLongReview(boardId);
                }
                return false;
            }
        });



    }
    private void requestModifyLongReview(String boardId){
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("writing", edtWriting.getText().toString());
            jsonObject.put("boardID", boardId);
            jsonObject.put("title", edtTitle.getText().toString());

//            Log.d(TAG, "requestModifyLongReview: token:"+sharedPreferences.getString("token", "")+"  id:"+boardId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.modifyLongReview);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "long Review modify: res:" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        onBackPressed();
                    } else if (response.getString("res").equals("201")) {
                        Toast.makeText(getApplicationContext(), "이미 작성된 리뷰가 있습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "리뷰 작성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
