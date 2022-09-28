package com.example.lotus_kproject2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

public class SearchActivity extends AppCompatActivity {
    EditText edtSearch;
//    FragmentManager childFragmentManager = getChildFragmentManager();
    private SearchResultActivity_main searchResultActivity_main = new SearchResultActivity_main();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        edtSearch = findViewById(R.id.edtSearch);

        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == keyEvent.ACTION_DOWN) && (keyCode == keyEvent.KEYCODE_ENTER)) {
                    Intent intent = new Intent(getApplicationContext(), SearchResultActivity_main.class);
                    intent.putExtra("searchWord", edtSearch.getText().toString());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }


}
