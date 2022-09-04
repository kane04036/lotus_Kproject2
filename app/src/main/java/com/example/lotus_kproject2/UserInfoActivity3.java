package com.example.lotus_kproject2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserInfoActivity3 extends AppCompatActivity {
    Spinner mbtiSpinner;
    ImageButton btnNextUserinfo3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo3);
        overridePendingTransition(R.anim.none, R.anim.none);


        mbtiSpinner = findViewById(R.id.mbtiSpinner);
        btnNextUserinfo3 = findViewById(R.id.btnNextUserinfo3);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
               R.array.mbti_array , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mbtiSpinner.setAdapter(adapter);

        btnNextUserinfo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoActivity3.this, UserInfoActivity4.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.none, R.anim.none);
    }
}
