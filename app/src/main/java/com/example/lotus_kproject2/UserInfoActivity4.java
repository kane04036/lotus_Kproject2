package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class UserInfoActivity4 extends AppCompatActivity {
    ImageButton btnNextUserinfo4;
    EditText edtRegisterNickname;
    TextView tvValidNickname;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo4);
        overridePendingTransition(R.anim.none, R.anim.none);

        btnNextUserinfo4 = findViewById(R.id.btnNextUserinfo4);
        edtRegisterNickname = findViewById(R.id.edtRegisterNickname);
        tvValidNickname = findViewById(R.id.tvValidNickname);

        btnNextUserinfo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validNickCheck();
            }
        });


    }
    public void validNickCheck(){
        RequestQueue Queue = Volley.newRequestQueue(UserInfoActivity4.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("form", 2);
            jsonObject.put("data", edtRegisterNickname.getText().toString());
            Log.d(TAG, "validNickCheck: nickname:"+edtRegisterNickname.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        String URL = "https://flask-sample-eezgq.run.goorm.io/umanager/register/check\n";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String res = response.getString("res");
                    Log.d(TAG, "onResponse: res:"+res);
                    if(res.equals("Success")){
                        Intent intent = new Intent(UserInfoActivity4.this, FragmentMainActivity.class);
                        startActivity(intent);
                    }else{
                        tvValidNickname.setText("이미 존재하는 닉네임입니다.");
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.none, R.anim.none);
    }
}
