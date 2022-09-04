package com.example.lotus_kproject2;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserInfoActivity2 extends AppCompatActivity {
    ImageButton btnNextUserinfo2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo2);
        overridePendingTransition(R.anim.none, R.anim.none);


        btnNextUserinfo2 = findViewById(R.id.btnNextUserinfo2);

        btnNextUserinfo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoActivity2.this, UserInfoActivity3.class);
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
