package com.example.lotus_kproject2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.horizontalexit, R.anim.none);
        setContentView(R.layout.fragment_search);

    }

    @Override
    public void finish() {
        super.finish();
        if(isFinishing())
            overridePendingTransition(R.anim.none, R.anim.horizontalenter);

    }
}
