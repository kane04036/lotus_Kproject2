package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewEditorActivity extends AppCompatActivity {
    EditText  edtTitle, edtReview;
    TextView tvChooseMov;
    String movName;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_editor);



        edtTitle = findViewById(R.id.edtTitle);
        edtReview = findViewById(R.id.edtReview);
        tvChooseMov = findViewById(R.id.tvChooseMov);

        Log.d(TAG, "onCreate: existMovie: " + getIntent().getIntExtra("existMovie", 0));
        if(getIntent().getIntExtra("existMovie",0)==1){
            movName = getIntent().getStringExtra("movName");
            Log.d(TAG, "onCreate: getString: movName: " + movName);
            tvChooseMov.setText(movName);

        }

        tvChooseMov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewEditorActivity.this, SearchMovForChooseActivity.class);
                startActivity(intent);
            }
        });



    }

}
