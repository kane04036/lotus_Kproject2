package com.example.lotus_kproject2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import jp.wasabeef.blurry.Blurry;

public class MovieDetail2 extends AppCompatActivity {
    View viewbackgorundImage;
    ImageView imageview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);

//        viewbackgorundImage = findViewById(R.id.viewBackgroundMovie);
//        imageview = findViewById(R.id.imageBackgroundMovie);
        Blurry.with(getApplicationContext()).capture(viewbackgorundImage).into(imageview);
    }
}
