package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

public class EditMbtiActivity extends AppCompatActivity {
    private Spinner spinner;
    private TextView btnUpdate;
    private MaterialToolbar appbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mbti);

        spinner = findViewById(R.id.spinnerMbtiForEdit);
        appbar = findViewById(R.id.topAppBarInEditMbti);
        btnUpdate = findViewById(R.id.btnUpdateMbti);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(EditMbtiActivity.this,
                R.array.mbti_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        spinner.setAdapter(adapter);

        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setEnabled(false);
                requestModifyMbti();
            }
        });

    }
    private void requestModifyMbti() {
        RequestQueue Queue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.loginData), Context.MODE_PRIVATE);

            jsonObject.put("token", sharedPreferences.getString("token", ""));
            jsonObject.put("mbti", spinner.getSelectedItemPosition());

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = getString(R.string.server) + getString(R.string.editMbti);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: modify mbti res:" + response.getString("res"));
                    if (response.getString("res").equals("200")) {
                        Toast.makeText(getApplicationContext(), "MBTI가 변경되었습니다.", Toast.LENGTH_SHORT).show();

                        Resources res = getResources();
                        String[] mbtiArray = res.getStringArray(R.array.mbti_array);
                        String userMbti = mbtiArray[spinner.getSelectedItemPosition()];

                        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.userData),Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.putString("mbti", userMbti);
                        editor1.commit();
                        onBackPressed();
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
