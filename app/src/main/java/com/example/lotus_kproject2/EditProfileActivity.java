package com.example.lotus_kproject2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import org.w3c.dom.Text;

public class EditProfileActivity extends AppCompatActivity {
    private TextView tvNickname, tvMbti;
    private SharedPreferences sharedPreferences;
    private MaterialToolbar appbar;

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences(getString(R.string.userData), Context.MODE_PRIVATE);
        tvNickname.setText(sharedPreferences.getString("nickname", ""));
        tvMbti.setText(sharedPreferences.getString("mbti", ""));
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        tvNickname = findViewById(R.id.tvNicknameInEditProfile);
        tvMbti = findViewById(R.id.tvMbtiInEditProfile);
        appbar = findViewById(R.id.topAppBarInEditProfile);

        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: nickname");
                Intent intent = new Intent(EditProfileActivity.this, EditNicknameActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        tvMbti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: mbti");
                Intent intent = new Intent(EditProfileActivity.this, EditMbtiActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });


    }
}
