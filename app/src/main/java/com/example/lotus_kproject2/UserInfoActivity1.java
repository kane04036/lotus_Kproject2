package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserInfoActivity1 extends AppCompatActivity {
    ImageButton btnNextUserinfo1;
    ImageView imageFemale, imageMale;
    Boolean femaleCheck = false, maleCheck = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        btnNextUserinfo1 = findViewById(R.id.btnNextUserinfo1);
        imageFemale = findViewById(R.id.imageFemale);
        imageMale = findViewById(R.id.imageMale);

        btnNextUserinfo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoActivity1.this, UserInfoActivity2.class);
                startActivity(intent);
            }
        });

        imageFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                femaleCheck = true;
                imageFemale.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
                if(maleCheck){
                    imageMale.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.DST);
                    maleCheck = false;
                }
            }
        });
        imageMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maleCheck = true;
                imageMale.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
                if(femaleCheck){
                    imageFemale.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.DST);
                    femaleCheck = false;
                }
            }
        });
    }
}
