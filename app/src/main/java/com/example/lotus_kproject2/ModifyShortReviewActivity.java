package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ModifyShortReviewActivity extends AppCompatActivity {
    private MaterialRatingBar ratingBar;
    private EditText edtShortReview;
    private TextView tvWritingCount, tvTotalWritingCount;
    private String movCode, boardId, star;
    private MaterialToolbar appbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_short_review);

        ratingBar = findViewById(R.id.ratingbarInModifyShortReview);
        edtShortReview = findViewById(R.id.edtModifyShortReview);
        tvWritingCount = findViewById(R.id.tvWritingCountInModifyShortReview);
        tvTotalWritingCount = findViewById(R.id.tvWritingTotalCountInModifyShortReview);
        appbar = findViewById(R.id.topAppBarInModifyShortReview);

        star = getIntent().getStringExtra("star");
        ratingBar.setRating(Float.parseFloat(star));
        edtShortReview.setText(getIntent().getStringExtra("writing"));
        movCode = getIntent().getStringExtra("movCode");
        boardId = getIntent().getStringExtra("boardId");
        tvWritingCount.setText(String.valueOf(edtShortReview.getText().length()));

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
                        requestModifyReview(edtShortReview.getText().toString(), boardId, star);
                        break;
                }
                return false;
            }
        });

        edtShortReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvWritingCount.setText(String.valueOf(edtShortReview.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvWritingCount.setText(String.valueOf(edtShortReview.getText().length()));

            }
        });
    }
    private void requestModifyReview(String writing, String boardId, String star){
        Log.d(TAG, "uploadShortReview");
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);
            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("writing", writing);
            jsonObject.put("boardID", boardId);
            jsonObject.put("star", star);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.modifyShortReview);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "short Review modify: res:" + response.getString("res"));
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
