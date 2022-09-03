package com.example.lotus_kproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button btnLogin;
    EditText edtLoginID, edtLoginPW;
    TextView tvLook, tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.btnLogin);
        edtLoginID = findViewById(R.id.edtLoginId);
        edtLoginPW = findViewById(R.id.edtLoginPw);
        tvLook = findViewById(R.id.tvLook);
        tvRegister = findViewById(R.id.tvRegister);

        tvLook.setPaintFlags(tvLook.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        tvRegister.setPaintFlags(tvRegister.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        tvLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}